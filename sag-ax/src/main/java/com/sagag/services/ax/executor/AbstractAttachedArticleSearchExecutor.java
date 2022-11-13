package com.sagag.services.ax.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.article.api.executor.AttachedArticleSearchExternalExecutor;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.article.api.request.VehicleRequest;
import com.sagag.services.ax.converter.price.AbstractPriceConverter;
import com.sagag.services.ax.request.AxPriceRequest;
import com.sagag.services.ax.request.AxPriceRequestBuilder;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.ax.utils.AxPriceUtils;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.BulkArticleResult;
import com.sagag.services.domain.sag.erp.PriceWithArticle;

import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class to get price and info attached article
 *
 */
@Slf4j
public abstract class AbstractAttachedArticleSearchExecutor<R>
    implements AttachedArticleSearchExternalExecutor<R> {

  @Autowired
  private List<AbstractPriceConverter> abstractPriceConverters;

  @Autowired
  private ArticleExternalService articleExtService;

  @Autowired
  private AppProperties appProps;

  public List<ArticleDocDto> updateAttachedArticles(final AttachedArticleSearchCriteria criteria) {

    final Map<String, BulkArticleResult> articlesRes = searchAttachedArticleIds(
        criteria.getAffiliate().getCompanyName(), criteria.getAttachedArticleRequestList());

    if (MapUtils.isEmpty(articlesRes)) {
      return Collections.emptyList();
    }

    return articlesRes.values().stream().map(mapBulkArticleToDto(criteria))
        .collect(Collectors.toList());
  }

  public Map<String, ArticleDocDto> bindingArticlesByKey(final List<ArticleDocDto> articles,
      final List<AttachedArticleRequest> attachedArticleRequestList,
      final boolean isCallShoppingCart) {

    final Map<String, ArticleDocDto> result = new HashMap<>();
    for (AttachedArticleRequest request : attachedArticleRequestList) {

      final Optional<ArticleDocDto> articleOpt =
          findAttachedArticle(articles, request.getAttachedArticleId());
      if (!articleOpt.isPresent()) {
        continue;
      }
      final ArticleDocDto article = articleOpt.get();
      // Use cart key for Shopping Cart and use articleAttachedId for Filter
      final String cartKey =
          isCallShoppingCart ? request.getCartKey() : request.getAttachedArticleId();
      article.setAmountNumber(request.getQuantity());
      article.setSalesQuantity(request.getSalesQuantity());
      reCalculateAttachedArticlePrice(article.getPrice(), article.getAmountNumber());

      // Clone new object for this article
      result.put(cartKey, SagBeanUtils.map(article, ArticleDocDto.class));
    }
    return result;
  }

  public static Optional<ArticleDocDto> findAttachedArticle(final List<ArticleDocDto> articles,
      final String attachedArticleId) {
    return articles.stream()
        .filter(article -> StringUtils.equals(article.getIdSagsys(), attachedArticleId))
        .findFirst();
  }

  public List<ArticleDocDto> updatePrice(final List<ArticleDocDto> articles,
      final AttachedArticleSearchCriteria criteria) {

    final Collection<ArticleRequest> articleRequests =
        AxArticleUtils.prepareArticleRequests(articles);
    final Optional<VehicleRequest> vehicleRequest =
            VehicleRequest.createErpVehicleRequest(criteria.getVehicle());
    final BasketPositionRequest basketRequest =
        new BasketPositionRequest(articleRequests, vehicleRequest);

    final AxPriceRequest priceRequest =
        new AxPriceRequestBuilder(Collections.singletonList(basketRequest))
            .customerNr(String.valueOf(criteria.getCustNr())).grossPrice(criteria.isGrossPrice())
            .priceDisplayTypeEnum(criteria.getPriceTypeDisplayEnum())
            .specialNetPriceArticleGroup(criteria.isSpecialNetPriceArticleGroup()).build();

    try {
      boolean forceToNotFinalCustomerUserForCalculationOfAttachedArticles = false;
      final Map<String, PriceWithArticle> priceRes =
          articleExtService.searchPrices(criteria.getCompanyName(), priceRequest,
              forceToNotFinalCustomerUserForCalculationOfAttachedArticles);

      // Update price info to article

      AbstractPriceConverter priceConverter = abstractPriceConverters.stream()
          .filter(converter -> converter
              .isValidConverter(forceToNotFinalCustomerUserForCalculationOfAttachedArticles))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("Not found compatiable price converter"));

      final List<ArticleDocDto> result = new ArrayList<>(articles);
      result.forEach(article -> {
        final PriceWithArticle price = priceRes.get(article.getIdSagsys());
        if (AxPriceUtils.isValidPrice(price)) {
          priceConverter.setNoPriceIfGrossLessThanNetPriceForSubArticle(price);
          article.setPrice(price);
          AxPriceUtils.updateDisplayedPrice(article, price);

          if (criteria.isCalculateVatPriceRequired()
              && Objects.nonNull(article.getPrice().getPrice())) {
            Optional<AttachedArticleRequest> attachedArticleRequestById =
                criteria.getAttachedArticleRequestById(article.getIdSagsys());
            Double vatRate = NumberUtils.DOUBLE_ZERO;
            if (attachedArticleRequestById.isPresent()) {
              vatRate = attachedArticleRequestById.get().getInheritVatRate();
            }
            article.getPrice().getPrice().setVatInPercent(vatRate);
            AxPriceUtils.updateVatRatePrice(price, vatRate);
          }
          doCalculatePriceForFinalCustomer(article, criteria);
        }});

      return result;
    } catch (final RestClientException ex) {
      log.error("Update article price from ERP has exception: ", ex);
      return new ArrayList<>(articles);
    }
  }

  protected void reCalculatePriceForDepositItem(final List<ArticleDocDto> filteredArticles,
      boolean finalCustomerUser, boolean finalCustomerHasNetPrice) {
    if (finalCustomerUser && finalCustomerHasNetPrice) {
      CollectionUtils.emptyIfNull(filteredArticles).forEach(art -> {
        art.setFinalCustomerNetPrice(art.getNetPrice().orElse(0.0));
      });
    }
  }

  private static Function<BulkArticleResult, ArticleDocDto> mapBulkArticleToDto(
      final AttachedArticleSearchCriteria criteria) {
    return bulkArticle -> {
      final String articleId = String.valueOf(bulkArticle.getArticleId());
      final ArticleDocDto article = new ArticleDocDto();
      article.setArticle(bulkArticle.getArticle());
      article.setArtid(articleId);
      article.setIdSagsys(articleId);
      criteria.getAttachedArticleRequestById(articleId).ifPresent(request -> {
        article.setAmountNumber(request.getQuantity());
        article.setSalesQuantity(request.getSalesQuantity());
      });
      return article;
    };
  }

  private Map<String, BulkArticleResult> searchAttachedArticleIds(String compName,
      List<AttachedArticleRequest> attachedArticles) {

    final List<String> articleIds = CollectionUtils.emptyIfNull(attachedArticles).stream()
        .map(AttachedArticleRequest::getAttachedArticleId).distinct().collect(Collectors.toList());
    if (CollectionUtils.isEmpty(articleIds)) {
      return Collections.emptyMap();
    }
    final List<List<String>> articleIdPartitions =
        ListUtils.partition(articleIds, appProps.getErpConfig().getMaxRequestSize());
    final Map<String, BulkArticleResult> articlesRes = new HashMap<>();
    for (final List<String> articleIdPartition : articleIdPartitions) {
      try {
        articlesRes.putAll(articleExtService.searchByArticleIds(compName, articleIdPartition));
      } catch (final RestClientException e) {
        log.error("Issue occurs on article request for articleIds: ", e);
      }
    }

    return articlesRes;
  }
}
