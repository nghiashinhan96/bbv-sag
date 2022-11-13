package com.sagag.services.ax.callable.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.UnaryOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.article.api.executor.callable.ChunkedRequestProcessor;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.article.api.request.VehicleRequest;
import com.sagag.services.ax.request.AxPriceRequest;
import com.sagag.services.ax.request.AxPriceRequestBuilder;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.ax.utils.AxPriceUtils;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.PriceWithArticle;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AxProfile
public class AxPriceAsyncCallableCreatorImpl implements ErpCallableCreator,
  ChunkedRequestProcessor<ArticleDocDto> {

  @Autowired
  private ArticleExternalService articleExtService;

  @Autowired
  private AppProperties appProps;

  @Override
  public Callable<List<ArticleDocDto>> create(ArticleSearchCriteria criteria, Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      return updatePrice(criteria);
    };
  }

  private List<ArticleDocDto> updatePrice(final ArticleSearchCriteria criteria) {
    return processPartitionsByList(criteria.getArticles(), updatePriceProcessor(criteria));
  }

  private UnaryOperator<List<ArticleDocDto>> updatePriceProcessor(ArticleSearchCriteria criteria) {
    return articles -> {
      final Collection<ArticleRequest> articleRequests =
          AxArticleUtils.prepareArticleRequests(articles);
      final Optional<VehicleRequest> vehicleRequest =
          VehicleRequest.createErpVehicleRequest(criteria.getVehicle());
      final BasketPositionRequest basketRequest =
          new BasketPositionRequest(articleRequests, vehicleRequest);

      final AxPriceRequest priceRequest =
          new AxPriceRequestBuilder(Collections.singletonList(basketRequest))
              .customerNr(criteria.getCustNr()).grossPrice(criteria.isGrossPrice())
              .priceDisplayTypeEnum(criteria.getPriceTypeDisplayEnum())
              .specialNetPriceArticleGroup(criteria.isSpecialNetPriceArticleGroup()).build();

      try {
        final Map<String, PriceWithArticle> priceRes = articleExtService
            .searchPrices(criteria.getCompanyName(), priceRequest, criteria.isFinalCustomerUser());

        // Update price info to article
        final List<ArticleDocDto> result = new ArrayList<>(articles);
        result.forEach(article -> {
          final PriceWithArticle price = priceRes.get(article.getIdSagsys());
          if (AxPriceUtils.isValidPrice(price)) {
            article.setPrice(price);
            AxPriceUtils.updateDisplayedPrice(article, price);

            if (criteria.isCalculateVatPriceRequired()
                && Objects.nonNull(article.getPrice().getPrice())) {
              //Use default vat rate if not exist article vat rate
              Double vatRate = AxPriceUtils.defaultVatRate(article, criteria.getVatRate());
              article.getPrice().getPrice().setVatInPercent(vatRate);
              AxPriceUtils.updateVatRatePrice(price, vatRate);
            }
          }
        });

        return result;
      } catch (final RestClientException ex) {
        log.error("Update article price from ERP has exception: ", ex);
        return new ArrayList<>(articles);
      }
    };
  }

  @Override
  public AsyncUpdateMode asyncUpdateMode() {
    return AsyncUpdateMode.PRICE;
  }

  @Override
  public int maxRequestSize() {
    return appProps.getErpConfig().getMaxRequestSize();
  }
}
