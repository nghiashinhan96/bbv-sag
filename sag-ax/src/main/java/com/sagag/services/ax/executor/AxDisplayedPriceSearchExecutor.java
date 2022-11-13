package com.sagag.services.ax.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.executor.ArticleErpExternalExecutorBuilders;
import com.sagag.services.article.api.executor.DisplayedPriceSearchExecutor;
import com.sagag.services.article.api.price.DisplayedPriceRequestCriteria;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.ax.domain.AxDisplayedPriceRequestBody;
import com.sagag.services.ax.request.AxPriceRequest;
import com.sagag.services.ax.request.AxPriceRequestBuilder;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.ax.utils.AxPriceUtils;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceRequest;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceRequestItem;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceResponseItem;
import com.sagag.services.domain.sag.erp.PriceWithArticle;

@Component
@AxProfile
public class AxDisplayedPriceSearchExecutor implements DisplayedPriceSearchExecutor {

  @Autowired
  private ArticleExternalService articleExtService;

  @Autowired
  private ArticleErpExternalExecutorBuilders artErpExtExecutorBuilders;

  @Override
  public List<DisplayedPriceResponseItem> execute(DisplayedPriceRequest priceRequest) {

    final List<DisplayedPriceResponseItem> result = new ArrayList<>();
    final List<DisplayedPriceRequestItem> singleBrandItems = priceRequest.getRequestItems().stream()
        .filter(DisplayedPriceRequestItem::isSingleBrandItem).collect(Collectors.toList());

    if (!CollectionUtils.isEmpty(singleBrandItems)) {
      final List<DisplayedPriceResponseItem> singleBrandItemResponses =
          getPriceForSingleBrandItems(singleBrandItems, priceRequest);

      result.addAll(singleBrandItemResponses);
    }

    final List<DisplayedPriceRequestItem> multipleBrandItems =
        priceRequest.getRequestItems().stream()
            .filter(DisplayedPriceRequestItem::isMultipleBrandItem).collect(Collectors.toList());

    if (!CollectionUtils.isEmpty(multipleBrandItems)) {
      final List<DisplayedPriceResponseItem> multipleBrandItemResponses =
          multipleBrandItems.stream().map(item -> getPriceForDisplayedPriceRequestItem(priceRequest,
              item))
          .collect(Collectors.toList());

      result.addAll(multipleBrandItemResponses);
    }

    return result;
  }

  private DisplayedPriceResponseItem getPriceForDisplayedPriceRequestItem(
      DisplayedPriceRequest dispPriceRequest, DisplayedPriceRequestItem requestItem) {

    final List<DisplayedPriceRequestCriteria> axPriceRequests = requestItem.getBrands().stream()
        .map(item -> createPriceRequestForEachBrandId(requestItem.getArticle(), item.getBrand(),
            item.getBrandId(), dispPriceRequest))
        .collect(Collectors.toList());

    final ServletRequestAttributes mainThreadAttrs =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    artErpExtExecutorBuilders.buildGetErpDisplayedPriceList(axPriceRequests, mainThreadAttrs)
    .join();

    final List<DisplayedPriceDto> prices =
        axPriceRequests.stream().map(DisplayedPriceRequestCriteria::getPrice)
            .filter(DisplayedPriceDto::isValidItem).collect(Collectors.toList());
    return DisplayedPriceResponseItem.builder().articleId(requestItem.getArticleId())
        .amount(requestItem.getAmount()).prices(prices).build();
  }

  /**
   * Returns price for multiple articles with only brand item.
   *
   */
  private List<DisplayedPriceResponseItem> getPriceForSingleBrandItems(
      List<DisplayedPriceRequestItem> requestItems, DisplayedPriceRequest dispPriceRequest) {

    final List<ArticleDocDto> articles = requestItems.stream()
        .map(DisplayedPriceRequestItem::getArticle).collect(Collectors.toList());
    final Collection<ArticleRequest> articleRequests =
        AxArticleUtils.prepareArticleRequests(articles);

    articleRequests.stream()
        .forEach(item -> requestItems.stream()
            .filter(requestItem -> requestItem.getArticleId().equals(String.valueOf(item.getId())))
            .findFirst().map(DisplayedPriceRequestItem::getBrandIdForSingleBrandItem)
            .ifPresent(item::setBrandId));

    final BasketPositionRequest basketRequest =
        new BasketPositionRequest(articleRequests, Optional.empty());

    final String companyName = dispPriceRequest.getCompanyName();
    final String custNr = dispPriceRequest.getCustNr();
    final PriceDisplayTypeEnum priceTypeDisplayEnum = dispPriceRequest.getPriceTypeDisplayEnum();

    final AxPriceRequest priceRequest =
        new AxPriceRequestBuilder(Collections.singletonList(basketRequest))
            .customerNr(custNr).grossPrice(false)
            .priceDisplayTypeEnum(priceTypeDisplayEnum).specialNetPriceArticleGroup(false).build();

    final Map<String, PriceWithArticle> priceRes =
        articleExtService.searchPrices(companyName, priceRequest,
            dispPriceRequest.isFinalCustomerUser());

    return requestItems.stream().map(item -> {
      final String articleId = item.getArticleId();

      final DisplayedPriceDto displayPriceDto = AxPriceUtils.calculateOepDisplayedPrice(
          priceRes.get(articleId), dispPriceRequest.getVatRate(),
          item.getBrandIdForSingleBrandItem(), item.getBrandForSingleBrandItem(), item.getAmount());

      return DisplayedPriceResponseItem.builder().articleId(articleId).amount(item.getAmount())
          .prices(displayPriceDto.isValidItem() ? Arrays.asList(displayPriceDto)
              : Collections.emptyList())
          .build();
    }).collect(Collectors.toList());
  }

  private AxDisplayedPriceRequestBody createPriceRequestForEachBrandId(ArticleDocDto article,
      String brand, Long brandId, DisplayedPriceRequest dispPriceRequest) {

    final String companyName = dispPriceRequest.getCompanyName();
    final String custNr = dispPriceRequest.getCustNr();
    final PriceDisplayTypeEnum priceTypeDisplayEnum = dispPriceRequest.getPriceTypeDisplayEnum();
    final boolean isFinalCustomerUser = dispPriceRequest.isFinalCustomerUser();
    final double vatRate = dispPriceRequest.getVatRate();

    final AxPriceRequest axPriceRequest =
        preparePriceRequest(article, brandId, custNr, priceTypeDisplayEnum);

    AxDisplayedPriceRequestBody requestBody = new AxDisplayedPriceRequestBody();
    requestBody.setBrand(brand);
    requestBody.setBrandId(brandId);
    requestBody.setArticleId(article.getIdSagsys());
    requestBody.setAmountNumber(article.getAmountNumber());
    requestBody.setAxPriceRequest(axPriceRequest);
    requestBody.setCompanyName(companyName);
    requestBody.setFinalCustomerUser(isFinalCustomerUser);
    requestBody.setVatRate(vatRate);

    return requestBody;
  }

  private AxPriceRequest preparePriceRequest(ArticleDocDto article, Long brandId, String custNr,
      PriceDisplayTypeEnum priceTypeDisplayEnum) {

    final Collection<ArticleRequest> articleRequests =
        AxArticleUtils.prepareArticleRequests(Arrays.asList(article));

    articleRequests.stream().forEach(item -> item.setBrandId(brandId));

    final BasketPositionRequest basketRequest =
        new BasketPositionRequest(articleRequests, Optional.empty());

    return new AxPriceRequestBuilder(Collections.singletonList(basketRequest))
        .customerNr(custNr).grossPrice(false)
        .priceDisplayTypeEnum(priceTypeDisplayEnum).specialNetPriceArticleGroup(false).build();
  }
}
