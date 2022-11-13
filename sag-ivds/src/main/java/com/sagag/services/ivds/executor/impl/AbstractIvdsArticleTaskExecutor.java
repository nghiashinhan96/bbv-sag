package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.VatRateDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchExternalExecutor;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.api.EshopGlobalSettingCacheService;
import com.sagag.services.hazelcast.api.VatRateCacheService;
import com.sagag.services.hazelcast.domain.EshopGlobalSettingDto;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.ivds.domain.ArticleExternalRequestOption;
import com.sagag.services.ivds.executor.IvdsAdditionalArticleTaskExecutor;
import com.sagag.services.ivds.executor.IvdsArticleElasticsearchTaskExecutor;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutor;
import com.sagag.services.ivds.utils.EshopBasketContextUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class AbstractIvdsArticleTaskExecutor implements IvdsArticleTaskExecutor {

  private static final String WSS_MAX_AVAILABILITY_DAY_RANGE = "wss_max_availability_day_range";

  @Autowired
  protected ContextService contextService;

  @Autowired
  private ArticleSearchExternalExecutor articleSearchExtExecutor;

  @Autowired
  private IvdsArticleElasticsearchTaskExecutor ivdsArticleElasticsearchTaskExecutor;

  @Autowired
  private IvdsAdditionalArticleTaskExecutor ivdsAdditionalArticleTaskExecutor;

  @Autowired
  @Qualifier("eshopGlobalSettingCacheServiceImpl")
  private EshopGlobalSettingCacheService eshopGlobalSettingCacheService;

  @Autowired
  private CustomerSettingsService customerSettingsService;

  @Autowired
  private VatRateCacheService vatRateCacheService;

  /**
   * Executes task ES info and custom request ERP info.
   *
   * <pre>
   * From ticket #1677 as Simon mentioned:
   * - The results of a free-text or article search should not be sorted on stock and/or availabilty.
   * - This has been discussed many times... the user needs to see the articles that match
   * and/or closest too the search terms.
   * - Please remove the sort by stock.
   * </pre>
   */
  @Override
  @LogExecutionTime
  public List<ArticleDocDto> executeTask(UserInfo user, List<ArticleDocDto> articles,
      Optional<VehicleDto> vehicleOpt, ArticleExternalRequestOption option) {
    Assert.notNull(user, "The current user info must not be null");
    log.info("Executing tasks: {} articles, external request option: {}", articles.size(), option);
    if (CollectionUtils.isEmpty(articles)) {
      return Collections.emptyList();
    }

    if (option == null) {
      return Collections.unmodifiableList(articles);
    }
    articles = new ArrayList<>(articles);

    if (!option.isIgnoredArtElasticsearch()) {
      ivdsArticleElasticsearchTaskExecutor.executeTask(articles, vehicleOpt,
              user.getSupportedAffiliate());
    }

    //#6475 : Filter Pseudo Article -> Not Request ERP.
    final List<ArticleDocDto> pseudoArticles = articles.stream()
        .filter(ArticleDocDto::isPseudoArticle).collect(Collectors.toList());

    // Filter out articles is pseudo out of main list.
    articles = articles.stream().filter(ArticleDocDto::isNotPseudoArticle)
        .collect(Collectors.toList());

    if (CollectionUtils.isEmpty(articles)) {
      return Collections.unmodifiableList(pseudoArticles);
    }

    if (!option.isCallErpRequest()) {
      return Collections.unmodifiableList(articles);
    }

    // Update articles info from external services
    final List<ArticleDocDto> updatedArticles =
        getExternalArticleInfo(user, articles, Optional.empty(), option);

    List<ArticleDocDto> joinedArticles = Stream
        .concat(updatedArticles.stream(), pseudoArticles.stream()).collect(Collectors.toList());
    removeTourtimeTableFromBackorderAvail(joinedArticles);
    return Collections.unmodifiableList(joinedArticles);
  }

  private static void removeTourtimeTableFromBackorderAvail(List<ArticleDocDto> joinedArticles) {
    CollectionUtils.emptyIfNull(joinedArticles).stream().forEach(article -> {
      if (article.hasAvailabilities()) {
        article.getAvailabilities().stream().forEach(avail -> {
          if (avail.isBackOrderTrue()) {
            avail.setTourTimeTable(Lists.emptyList());
          }
        });
      }
    });
  }

  private List<ArticleDocDto> getExternalArticleInfo(UserInfo user, List<ArticleDocDto> articles,
          Optional<VehicleDto> vehicleOpt, ArticleExternalRequestOption option) {
    final ArticleSearchCriteria.ArticleSearchCriteriaBuilder builder =
        initArticleSearchCriteriaBuilder(user, articles, vehicleOpt.orElse(null), option);

    this.loadUserContextAndAvailabilitiesInfo(builder, user, option);

    // Execute get article info from external ERP AX
    final ArticleSearchCriteria criteria = builder.build();
    final List<ArticleDocDto> updatedArticles = articleSearchExtExecutor.execute(criteria);
    log.info("[BBV] executeTask: {} updated articles", updatedArticles.size());
    ivdsAdditionalArticleTaskExecutor.executeTask(updatedArticles, option.isFilterArticleBefore(),
        option.isUpdatePrice());

    // BUG#3030: Reset availabilities information if call without update availability request
    // to trigger call avails in batch on demand
    if (!criteria.isUpdateAvailability()) {
      updatedArticles.stream().filter(article -> !article.isAvailRequested())
          .forEach(article -> article.setAvailabilities(null));
    }

    return updatedArticles;
  }

  /**
   * Initializes the article search external criteria builder to query with external ERP.
   *
   * @param user
   * @param updatedArticles
   * @param vehicleDto
   * @param option
   * @return the builder object of @{link ArticleSearchCriteria.ArticleSearchCriteriaBuilder}
   */
  public ArticleSearchCriteria.ArticleSearchCriteriaBuilder initArticleSearchCriteriaBuilder(
      UserInfo user, List<ArticleDocDto> updatedArticles, VehicleDto vehicleDto,
      ArticleExternalRequestOption option) {
    boolean caculateVatRate = calculateVatPriceRequired(user);

    updateVatRate(updatedArticles, caculateVatRate);

    ArticleSearchCriteria.ArticleSearchCriteriaBuilder builder = ArticleSearchCriteria.builder()
        .isCartMode(true)
        .affiliate(user.getSupportedAffiliate())
        .companyName(user.getCompanyName())
        .custNr(user.getCustNrStr())
        .defaultBrandId(user.getDefaultBranchId())
        .articles(updatedArticles)
        .vehicle(vehicleDto)
        .filterArticleBefore(option.isFilterArticleBefore())
        .updatePrice(option.isUpdatePrice())
        .updateStock(option.isUpdateStock())
        .updateAvailability(option.isUpdateAvailability())
        .availabilityUrl(user.getAvailabilityUrl())
        .priceTypeDisplayEnum(user.getSettings().getPriceTypeDisplayEnum())
        .language(user.getLanguage())
        .additional(option.getAdditional())
        .vatRate(user.getSettings().getVatRate())
        .isDropShipment(BooleanUtils.toBoolean(user.getSettings().isDropShipmentAvailability()))
        .custApprovalTypes(Collections.emptyList())
        .calculateVatPriceRequired(caculateVatRate);

    builder.isFinalCustomerUser(user.isFinalUserRole());
    builder.wssDeliveryProfile(user.getSettings().getWssDeliveryProfile());
    builder.wholeSalerHasNetPrice(user.isWholeSalerHasNetPrice());
    builder.wssOrgId(user.getOrganisationId());
    builder.finalCustomerOrgId(user.getFinalCustomerOrgId());
    builder.isFinalCustomerHasNetPrice(user.isFinalCustomerHasNetPrice());

    if (Objects.nonNull(option.getAdditional())) {
      builder.isExcludeSubArticles(
        BooleanUtils.isTrue(option.getAdditional().getIsExcludeSubArticles()));
    }

    if (user.hasCust()) {
      final Customer customer = user.getCustomer();
      builder.grantedBranches(ListUtils.emptyIfNull(customer.getGrantedBranches()));
      builder.allowShowPfandArticle(customer.isAllowShowPfandArticle());
      builder.custDisposalNumber(customer.getDisposalNumber());
      builder.custApprovalTypes(customer.getCustApprovalTypes());
    }

    if (user.isFinalUserRole() && user.hasAvailabilityPermission()) {
      eshopGlobalSettingCacheService.getSettingByCode(WSS_MAX_AVAILABILITY_DAY_RANGE)
      .map(EshopGlobalSettingDto::getSettingValue)
      .ifPresent(builder::wssMaxAvailabilityDayRange);
    }

    final Integer finalCustomerOrgId;
    if (user.isFinalUserRole()) {
      finalCustomerOrgId = user.getFinalCustomerOrgId();
    } else {
      finalCustomerOrgId = option.getFinalCustomerOrgId();
    }

    if (user.isWholeSalerHasNetPrice() && finalCustomerOrgId != null) {
      final CustomerSettings custSettings =
          customerSettingsService.findSettingsByOrgId(finalCustomerOrgId);
      if (custSettings.isNetPriceView()) {
        builder.isFinalCustomerHasNetPrice(custSettings.isNetPriceView());
        builder.finalCustomerMarginGroup(custSettings.getWssMarginGroup());
      }
    }

    return builder;
  }

  public boolean calculateVatPriceRequired(UserInfo user) {
    return !StringUtils.isEmpty(user.getSettings().getVatTypeDisplay());
  }

  public void updateVatRate(List<ArticleDocDto> articles, boolean caculateVatRate) {
    if (!caculateVatRate) {
      return;
    }

    Map<String, Double> artVatRateMap = vatRateCacheService
        .getCacheVatRateByArticleIds(
            articles.stream().map(ArticleDocDto::getArtid).collect(Collectors.toList()))
        .stream().collect(Collectors.toMap(VatRateDto::getArtId, VatRateDto::getCustomVatRate));

    articles.stream().forEach(artDto -> artDto.setVatRate(artVatRateMap.get(artDto.getArtid())));
  }

  /**
   * Loads the user context and availabilities info to calculation.
   *
   * @param builder
   * @param user
   * @param option
   */
  private void loadUserContextAndAvailabilitiesInfo(
      ArticleSearchCriteria.ArticleSearchCriteriaBuilder builder, UserInfo user,
      ArticleExternalRequestOption option) {
    if (!option.isUpdateAvailability() && !option.isUpdateStock()) {
      return;
    }

    final EshopBasketContext basketContext = contextService.getBasketContext(user.key());
    builder.deliveryType(
        EshopBasketContextUtils.findSendMethod(basketContext, ErpSendMethodEnum.TOUR.name()));
    final String pickupBranchId = contextService.getPickupBranchId(user);
    builder.pickupBranchId(pickupBranchId);

    if (option.isUpdateAvailability() || option.isUpdateStock()) {
      builder.addressId(EshopBasketContextUtils.findDeliveryAddressId(basketContext,
          user.getDeliveryAddressId()));
      this.loadAvailabilitiesInfo(builder, user, pickupBranchId);
    }
  }

  /**
   * Loads the pre-availabilities info to calculation.
   *
   * @param builder
   * @param user
   * @param pickupBranchId
   */
  protected void loadAvailabilitiesInfo(ArticleSearchCriteria.ArticleSearchCriteriaBuilder builder,
      UserInfo user, String pickupBranchId) {
    // Do nothing as default
  }

}
