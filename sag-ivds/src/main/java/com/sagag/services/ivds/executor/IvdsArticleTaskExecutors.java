package com.sagag.services.ivds.executor;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.article.api.executor.AttachedArticleSearchExternalExecutor;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.VehicleUsageDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceRequest;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceRequestItem;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceResponseItem;
import com.sagag.services.elasticsearch.api.ArticleVehiclesSearchService;
import com.sagag.services.elasticsearch.domain.article.ArticleVehicles;
import com.sagag.services.ivds.domain.ArticleExternalRequestOption;
import com.sagag.services.ivds.request.ErpInfoRequest;
import com.sagag.services.ivds.utils.VehicleUsagesBuilder;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class IvdsArticleTaskExecutors {

  @Autowired
  private IvdsArticleTaskExecutor ivdsArticleTaskExecutor;

  @Autowired
  private AttachedArticleSearchExternalExecutor<Map<String, ArticleDocDto>> attachedArticleSearchExecutor;

  @Autowired
  private ArticleVehiclesSearchService articleVehiclesSearchService;

  @Autowired
  private IvdsDisplayedPricesArticleTaskExecutor ivdsDisplayPricesArticleTaskExecutor;

  @Autowired
  private IvdsArticleElasticsearchTaskExecutor ivdsArticleElasticsearchTaskExecutor;

  /**
   * Executes update ES info without ERP info.
   *
   */
  public List<ArticleDocDto> executeTaskWithoutErp(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle) {

    final ArticleExternalRequestOption option = ArticleExternalRequestOption.builder().build();
    return ivdsArticleTaskExecutor.executeTask(user, articles, vehicle, option);
  }

  /**
   * Executes task update ES info and full ERP info.
   *
   */
  public List<ArticleDocDto> executeTaskWithFullRequest(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
      final Optional<AdditionalSearchCriteria> additional) {

    final ArticleExternalRequestOption option = ArticleExternalRequestOption.builder()
        .callErpRequest(true).filterArticleBefore(true).additional(defaultAdditional(additional))
        .updatePrice(true).updateAvailability(true).build();
    return ivdsArticleTaskExecutor.executeTask(user, articles, vehicle, option);
  }

  /**
   * Executes task ES info and ERP price info.
   *
   */
  public List<ArticleDocDto> executeTaskWithPrice(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
      final Optional<AdditionalSearchCriteria> additional) {

    return executeTaskWithPrice(user, articles, vehicle, Optional.empty(), additional);
  }

   public List<ArticleDocDto> executeTaskWithPrice(final UserInfo user,
       final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
       final Optional<Integer> finalCustomerOrgIdForWholesalerViewOpt,
       final Optional<AdditionalSearchCriteria> additional) {

     final ArticleExternalRequestOption option = ArticleExternalRequestOption.builder()
         .callErpRequest(true).updatePrice(true).additional(defaultAdditional(additional))
         .finalCustomerOrgId(finalCustomerOrgIdForWholesalerViewOpt.orElse(null)).build();
     return ivdsArticleTaskExecutor.executeTask(user, articles, vehicle, option);
   }

   public List<ArticleDocDto> executeTaskWithPriceOnlyWithoutVehicle(final UserInfo user,
       final List<ArticleDocDto> articles, Optional<Integer> finalCustomerOrgIdOpt,
       final Optional<AdditionalSearchCriteria> additional) {

     return executeTaskWithPrice(user, articles, Optional.empty(), finalCustomerOrgIdOpt,
         additional);
   }

  public List<ArticleDocDto> executeTaskWithStock(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
      final Optional<AdditionalSearchCriteria> additional) {

    final ArticleExternalRequestOption option = ArticleExternalRequestOption.builder()
        .callErpRequest(true).updateStock(true).additional(defaultAdditional(additional)).build();
    return ivdsArticleTaskExecutor.executeTask(user, articles, vehicle, option);
  }

  /**
   * Executes task ES info and availability info.
   */
  @LogExecutionTime
  public List<ArticleDocDto> executeTaskWithAvailability(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
      final Optional<AdditionalSearchCriteria> additional) {
    return executeTaskWithAvailability(user, articles, vehicle, additional, false);
  }

  @LogExecutionTime
  public List<ArticleDocDto> executeTaskWithAvailabilityAndIgnoredArtElasticsearch(
      final UserInfo user, final List<ArticleDocDto> articles,
      final Optional<VehicleDto> vehicle,
      final Optional<AdditionalSearchCriteria> additional) {
    return executeTaskWithAvailability(user, articles, vehicle, additional, true);
  }

  private List<ArticleDocDto> executeTaskWithAvailability(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
      final Optional<AdditionalSearchCriteria> additional, final boolean ignoredArtElasticsearch) {

    final ArticleExternalRequestOption option =
        ArticleExternalRequestOption.builder().callErpRequest(true).updateAvailability(true)
            .ignoredArtElasticsearch(ignoredArtElasticsearch)
            .additional(defaultAdditional(additional)).build();
    return ivdsArticleTaskExecutor.executeTask(user, articles, vehicle, option);
  }

  public List<ArticleDocDto> executeTaskWithAvailabilityWithoutVehicle(final UserInfo user,
      final List<ArticleDocDto> articles) {
    return executeTaskWithAvailability(user, articles, Optional.empty(), Optional.empty());
  }

  public List<ArticleDocDto> executeTaskWithAvailabilityWithoutVehicle(final UserInfo user,
      final List<ArticleDocDto> articles, Optional<AdditionalSearchCriteria> additional) {
    return executeTaskWithAvailability(user, articles, Optional.empty(), additional);
  }

  @LogExecutionTime
  public List<ArticleDocDto> executeTaskPriceAndAvailability(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle) {

    final ArticleExternalRequestOption option = ArticleExternalRequestOption.builder()
        .callErpRequest(true).updatePrice(true).updateAvailability(true).build();
    return ivdsArticleTaskExecutor.executeTask(user, articles, vehicle, option);
  }

  public List<ArticleDocDto> executeTaskPriceAndAvailabilityWithoutVehicle(final UserInfo user,
      final List<ArticleDocDto> articles) {
    return executeTaskPriceAndAvailability(user, articles, Optional.empty());
  }

  /**
   * Executes task ES info and ERP article, price and stock info.
   *
   */
  @LogExecutionTime
  public List<ArticleDocDto> executeTaskWithPriceAndStock(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
      final Optional<AdditionalSearchCriteria> additional) {
    return executeTaskWithPriceAndStock(user, articles, vehicle, Optional.empty(), additional);
  }

  public List<ArticleDocDto> executeTaskWithPriceAndStock(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
      final Optional<Integer> finalCustomerOrgIdForWholesalerViewOpt,
      final Optional<AdditionalSearchCriteria> additional) {

    final ArticleExternalRequestOption option =
        ArticleExternalRequestOption.builder().callErpRequest(true).filterArticleBefore(true)
            .updatePrice(true).updateStock(true).additional(defaultAdditional(additional))
            .finalCustomerOrgId(finalCustomerOrgIdForWholesalerViewOpt.orElse(null)).build();
    return ivdsArticleTaskExecutor.executeTask(user, articles, vehicle, option);
  }

  public List<ArticleDocDto> executeTaskWithPriceAndStockWithoutVehicle(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<AdditionalSearchCriteria> additional) {
    return executeTaskWithPriceAndStock(user, articles, Optional.empty(), additional);
  }

  public List<ArticleDocDto> executeTaskStockWithoutVehicle(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<AdditionalSearchCriteria> additional) {
    return executeTaskWithStock(user, articles, Optional.empty(), additional);
  }

  /**
   * Returns the list of attached articles.
   *
   * @param user
   * @param attachedAticleRequestList
   * @param vehicle
   * @return the list of attached articles
   */
  public Map<String, ArticleDocDto> executeTaskWithAttachedArticles(final UserInfo user,
      final List<AttachedArticleRequest> attachedAticleRequestList,
      final Optional<VehicleDto> vehicle) {
    return getAttachedArticleInfo(user, attachedAticleRequestList, vehicle);
  }

  private Map<String, ArticleDocDto> getAttachedArticleInfo(UserInfo user,
          List<AttachedArticleRequest> attachedAticleRequestList, Optional<VehicleDto> vehicle) {
    final SupportedAffiliate affiliate = SupportedAffiliate.fromCompanyName(user.getCompanyName());
    final WssDeliveryProfileDto wssDeliveryProfile = user.getSettings().getWssDeliveryProfile();
    final AttachedArticleSearchCriteria criteria =
        AttachedArticleSearchCriteria.createcreateArticleRequest(affiliate, user.getCustomer(),
                attachedAticleRequestList, vehicle.orElse(null), wssDeliveryProfile);
    criteria.setPriceTypeDisplayEnum(user.getSettings().getPriceTypeDisplayEnum());
    criteria.setFinalCustomerUser(user.isFinalUserRole());
    criteria.setFinalCustomerHasNetPrice(user.isFinalCustomerHasNetPrice());
    return attachedArticleSearchExecutor.execute(criteria);
  }

  /**
   * Executes update ES info with ERP article info.
   *
   */
  public List<ArticleDocDto> executeTaskWithErpArticle(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
      final Optional<AdditionalSearchCriteria> additional) {
    return executeTaskWithErpArticle(user, articles, vehicle, additional, false);
  }

  public List<ArticleDocDto> executeTaskWithErpArticle(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<AdditionalSearchCriteria> additional) {
    return executeTaskWithErpArticle(user, articles, Optional.empty(), additional, false);
  }

  public List<ArticleDocDto> executeTaskWithErpArticleAndIgnoredElasticsearch(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<AdditionalSearchCriteria> additional) {
    return executeTaskWithErpArticle(user, articles, Optional.empty(), additional, true);
  }

  private List<ArticleDocDto> executeTaskWithErpArticle(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
      final Optional<AdditionalSearchCriteria> additional, boolean ignoredElasticsearch) {

    final ArticleExternalRequestOption option = ArticleExternalRequestOption.builder()
        .callErpRequest(true).filterArticleBefore(true).additional(defaultAdditional(additional))
        .ignoredArtElasticsearch(ignoredElasticsearch).build();
    return ivdsArticleTaskExecutor.executeTask(user, articles, vehicle, option);
  }

  public void executeTaskElasticsearch(final List<ArticleDocDto> articles,
      final Optional<VehicleDto> vehicle, final SupportedAffiliate affiliate) {
    if (CollectionUtils.isEmpty(articles)) {
      return;
    }
    ivdsArticleElasticsearchTaskExecutor.executeTask(articles, vehicle, affiliate);
  }

  /**
   * Returns available vehicles use for a selected article.
   *
   * @param artId article artid
   * @return a list of all available branches including vehicles
   */
  public List<VehicleUsageDto> executeTaskWithVehicleUsages(String articleId) {
    if (StringUtils.isEmpty(articleId)) {
      final String errorMessage = "The article id should not be null or blank";
      log.warn(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    }
    final List<ArticleVehicles> articleVehicles =
        articleVehiclesSearchService.searchArticleVehiclesByArtId(articleId);
    return new VehicleUsagesBuilder(articleVehicles).build();
  }

  private static AdditionalSearchCriteria defaultAdditional(
      Optional<AdditionalSearchCriteria> additional) {
    return additional.orElse(null);
  }

  public List<DisplayedPriceResponseItem> executeTaskWithArticleDisplayPrices(
      List<DisplayedPriceRequestItem> requestItems, UserInfo user) {
    PriceDisplayTypeEnum priceDisplayTypeEnum = user.getSettings().getPriceTypeDisplayEnum();
    DisplayedPriceRequest request = DisplayedPriceRequest.builder().requestItems(requestItems)
        .companyName(user.getCompanyName()).custNr(user.getCustNrStr())
        .priceTypeDisplayEnum(priceDisplayTypeEnum).isFinalCustomerUser(user.isFinalUserRole())
        .vatRate(user.getSettings().getVatRate())
        .build();

    return ivdsDisplayPricesArticleTaskExecutor.executeTaskWithArticleDisplayPrices(user, request);
  }

  /**
   * Executes task ES info and ERP article, price, availability and stock info on demand.
   *
   */
  @LogExecutionTime
  public List<ArticleDocDto> executeTaskWithSpecificErpInfoRequest(final UserInfo user,
      final List<ArticleDocDto> articles, final Optional<VehicleDto> vehicle,
      final ErpInfoRequest erpInfoRequest, final Optional<AdditionalSearchCriteria> additional) {

    final ArticleExternalRequestOption.ArticleExternalRequestOptionBuilder optionBuilder =
        extractErpSyncInfoRequest(erpInfoRequest, additional);

    return ivdsArticleTaskExecutor.executeTask(user, articles, vehicle, optionBuilder.build());
  }

  @LogExecutionTime
  public Page<ArticleDocDto> executeTaskStockOnlyWithoutVehicle(final UserInfo user,
      final Page<ArticleDocDto> articles, final Optional<AdditionalSearchCriteria> additional) {
    if (!articles.hasContent()) {
      return Page.empty(articles.getPageable());
    }
    final List<ArticleDocDto> preferedArticles = articles.getContent();
    final List<ArticleDocDto> updatedArticles =
        executeTaskWithStock(user, preferedArticles, Optional.empty(), additional);

    return new PageImpl<>(updatedArticles, articles.getPageable(), articles.getTotalElements());
  }

  @LogExecutionTime
  public List<ArticleDocDto> executeTaskAvailabilitiesOnlyWithoutVehicle(final UserInfo user,
      final List<ArticleDocDto> articles, Optional<AdditionalSearchCriteria> additional) {
    if (CollectionUtils.isEmpty(articles)) {
      return Lists.newArrayList();
    }
    return executeTaskWithAvailabilityWithoutVehicle(user, articles, additional);
  }

  public ArticleExternalRequestOption.ArticleExternalRequestOptionBuilder extractErpSyncInfoRequest(
      final ErpInfoRequest erpInfoRequest, final Optional<AdditionalSearchCriteria> additional) {
    final ArticleExternalRequestOption.ArticleExternalRequestOptionBuilder optionBuilder =
        ArticleExternalRequestOption.builder().callErpRequest(true)
            .additional(defaultAdditional(additional));

    Optional.ofNullable(erpInfoRequest).map(ErpInfoRequest::isPriceRequested)
        .map(BooleanUtils::isTrue).ifPresent(optionBuilder::updatePrice);

    Optional.ofNullable(erpInfoRequest).map(ErpInfoRequest::isStockRequested)
        .map(BooleanUtils::isTrue).ifPresent(optionBuilder::updateStock);

    Optional.ofNullable(erpInfoRequest).map(ErpInfoRequest::isAvailabilityRequested)
        .map(BooleanUtils::isTrue).ifPresent(optionBuilder::updateAvailability);
    return optionBuilder;
  }

}
