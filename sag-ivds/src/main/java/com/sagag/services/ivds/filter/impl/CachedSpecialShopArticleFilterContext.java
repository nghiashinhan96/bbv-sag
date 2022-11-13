package com.sagag.services.ivds.filter.impl;

import com.google.common.primitives.Doubles;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.availability.externalvendor.ExternalStockFinder;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.ax.domain.vendor.VenExternalStockSearchCriteria;
import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.api.DeliveryProfileCacheService;
import com.sagag.services.hazelcast.api.ExternalVendorCacheService;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.filter.ArticleFilterContext;
import com.sagag.services.ivds.filter.ICachedArticleFilter;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.promotion.impl.StockComparator;
import com.sagag.services.ivds.promotion.reorder.TyreArticleReoder;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.utils.EshopBasketContextUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CachedSpecialShopArticleFilterContext extends ArticleFilterContext
    implements ICachedArticleFilter {

  private static final int NO_REQUEST_AVAIL_INDX = -1;

  private static final String CONTEXT_KEY_NOT_BLAK_MSG = "The given context key must not be empty";

  @Autowired
  private ContextService contextService;

  @Autowired
  private TyreArticleReoder tyreArticleReorder;

  @Autowired
  private ExternalVendorCacheService externalVendorCacheService;

  @Autowired
  private DeliveryProfileCacheService deliveryProfileCacheService;

  @Autowired
  private ExternalStockFinder externalStockFinder;

  /**
   * Returns the response of articles filtering in cached.
   *
   * @param user the current user login
   * @param filterMode the filtering mode
   * @param request the filtering request
   * @param pageable the paging object
   * @return the result of {@link ArticleFilteringResponseDto}
   */
  @Override
  public ArticleFilteringResponseDto execute(final UserInfo user, final FilterMode filterMode,
      final ArticleFilterRequest request, final Pageable pageable,
      final Optional<AdditionalSearchCriteria> additional) {

    final List<String> promotionBrands =
        getNowPromotionBrands(filterMode.getShopType(), user.getAffiliateShortName()).stream()
            .map(StringUtils::upperCase).sorted(String.CASE_INSENSITIVE_ORDER::compare)
            .collect(Collectors.toList());

    // Generate context key if request is empty.
    final String contextKey = contextKey(request.getContextKey(), user.key());

    // If first page, query all records(maximum is 500 records) and save to hazelcast
    if (!pageable.hasPrevious()) {
      Pageable pageRequest = PageUtils.defaultPageable(request.getTotalElementsOfSearching());
      FilteredArticleAndAggregationResponse esResponse =
          getArticleFilterResponse(user, filterMode, request, pageRequest);
      storeArticlesInCached(user, esResponse, contextKey, filterMode, promotionBrands, false);
    }
    return filterArticlesInCached(user, contextKey, pageable, filterMode, promotionBrands);
  }

  @Override
  public boolean supportMode(FilterMode filterMode) {
    return filterMode.isStoredInCachedSpecialShop();
  }

  @Override
  public void storeArticlesInCached(UserInfo user, FilteredArticleAndAggregationResponse response,
      String contextKey, FilterMode mode, List<String> promotionBrands,
      boolean isUseMulitipleLevelAgg) {
    Assert.hasText(contextKey, CONTEXT_KEY_NOT_BLAK_MSG);
    final List<ArticleDocDto> articles = response.getArticles().getContent();
    List<ArticleDocDto> joinedArticles = findAndSortStock(user, response.getArticles(),
        Optional.of(AdditionalSearchCriteria.builder().isExcludeSubArticles(!mode.isTyres())
            .build()));

    List<ArticleDocDto> joinedArticlesSortedByStock = mode.isTyres()
        ? joinedArticles : super.sortArticleStockDesc(joinedArticles);

    if (mode.isTyres()) {
      findExternalStock(joinedArticlesSortedByStock, user);
      joinedArticlesSortedByStock =
          articles.stream().sorted(StockComparator.getInstance()).collect(Collectors.toList());

    // #1194: Tyres: Tuning improvements - Point 1.
      updateStockForTyresOfCarVehicle(joinedArticlesSortedByStock);
      tyreArticleReorder.reorderFirstTime(joinedArticlesSortedByStock, ListUtils.emptyIfNull(promotionBrands));
    }
    storeCachedArticlesResultWithNoRequestAvail(joinedArticlesSortedByStock,
        response.getAggregations(), mode,
        contextKey, isUseMulitipleLevelAgg);
  }

  private void findExternalStock(List<ArticleDocDto> articles, UserInfo user) {
    final List<ArticleDocDto> notOnStockArticles =
        articles.stream().filter(ArticleDocDto::isNotOnStock).collect(Collectors.toList());

    final EshopBasketContext basketContext = contextService.getBasketContext(user.key());
    String deliveryType =
        EshopBasketContextUtils.findSendMethod(basketContext, ErpSendMethodEnum.TOUR.name());

    VenExternalStockSearchCriteria externalStockCriteria =
        VenExternalStockSearchCriteria.builder().companyName(user.getCompanyName())
            .deliveryType(deliveryType).pickupBranch(contextService.getPickupBranchId(user))
            .defaultBranch(user.getDefaultBranchId())
            .externalVendors(externalVendorCacheService.findAll())
            .deliveryProfiles(deliveryProfileCacheService.findAll()).build();
    List<ArticleDocDto> returnedArticles =
        externalStockFinder.findStocks(notOnStockArticles, externalStockCriteria);

    notOnStockArticles.stream().forEach(item -> updateExternalStock(item, returnedArticles));
  }

  private void updateExternalStock(ArticleDocDto targetArt, List<ArticleDocDto> returnedArticles) {
    returnedArticles.stream().filter(item -> item.getIdSagsys().equals(targetArt.getIdSagsys()))
        .forEach(art -> targetArt.setExternalStock(art.getExternalStock()));
  }

  protected Function<List<ArticleDocDto>, List<ArticleDocDto>> updateStockForArticles(
      final UserInfo user, FilterMode mode) {
    // Exclude sub articles with tyres search
    return partition -> findAndSortStock(user, new PageImpl<>(partition), Optional.of(
        AdditionalSearchCriteria.builder().isExcludeSubArticles(!mode.isTyres()).build()));
  }

  protected Map<String, List<ArticleFilterItem>> buildArticleFilterItems(
      final Map<String, List<SagBucket>> aggregations, final FilterMode filterMode,
      final boolean isUseMulitipleLevelAgg) {
    if (isUseMulitipleLevelAgg) {
      log.debug("Building within multiple level aggregation mode");
      return buildSubArticleFilterItems(aggregations);
    }
    log.debug("Building without multiple level aggregation mode");
    return this.buildArticleFilterItems(aggregations, filterMode);
  }

  protected void storeCachedArticlesResultWithNoRequestAvail(final List<ArticleDocDto> articles,
      final Map<String, List<SagBucket>> aggregations, final FilterMode filterMode,
      final String contextKey, final boolean isUseMulitipleLevelAgg) {

    final Map<String, List<ArticleFilterItem>> filterItems =
        buildArticleFilterItems(aggregations, filterMode, isUseMulitipleLevelAgg);
    storeCachedArticlesResult(articles, filterItems, contextKey, NO_REQUEST_AVAIL_INDX);
  }

  private void storeCachedArticlesResult(final List<ArticleDocDto> articles,
      final Map<String, List<ArticleFilterItem>> filters, final String contextKey,
      final int availRequestPageForTheBunchOfArticles) {
    // Save to hazelcast
    contextService.storeCachedArticlesResult(ofResult(new PageImpl<>(articles), filters, contextKey,
        availRequestPageForTheBunchOfArticles));
  }

  private static List<ArticleDocDto> updateStockForTyresOfCarVehicle(List<ArticleDocDto> articles) {
    articles.stream().forEach(article -> {
      // Update stock is not available
      if (!article.isTyreMotorbikeArticle() && isLessThanMinTyreStock().test(article)) {
        article.setStock(null);
        article.setTotalAxStock(null);
      }
    });
    return articles;
  }

  private static Predicate<ArticleDocDto> isLessThanMinTyreStock() {
    return article -> article.hasStock()
        && Doubles.compare(TyreConstants.MENGE_OF_TYRES_CAR_VEHICLE, article.getStockNr()) > 0;
  }

  @Override
  public ArticleFilteringResponseDto filterArticlesInCached(UserInfo user, String contextKey,
      Pageable pageable, FilterMode mode, List<String> promotionBrands) {
    final ArticleFilteringResponseDto response = getCachedAllArticleResult(contextKey);
    if (response == null || !response.hasContent()) {
      return emptyResult(contextKey);
    }

    final List<ArticleDocDto> curArticles;
    if (mode.isTyres() || mode.isWSPSearch()) {
      curArticles =
          getArticlesSortByAvailabitlity(user, response, pageable, contextKey, promotionBrands);
    } else {
      curArticles = getDefaultArticles(response, pageable);
    }
    return ofResult(new PageImpl<>(curArticles, pageable, response.totalElements()),
        response.getFilters(), contextKey);
  }

  protected ArticleFilteringResponseDto getCachedAllArticleResult(String contextKey) {
    Assert.hasText(contextKey, CONTEXT_KEY_NOT_BLAK_MSG);
    return contextService.getCachedArticlesResult(contextKey);
  }

  private List<ArticleDocDto> getDefaultArticles(ArticleFilteringResponseDto response,
      Pageable pageable) {
    List<ArticleDocDto> articles =
        ListUtils.partition(response.getArticles().getContent(), pageable.getPageSize())
            .get(pageable.getPageNumber());
    articles = new ArrayList<>(articles);
    return articles;
  }

  private List<ArticleDocDto> getArticlesSortByAvailabitlity(UserInfo user,
      ArticleFilteringResponseDto response, Pageable pageable, String contextKey,
      List<String> brands) {
    List<ArticleDocDto> articles;
    final SupportedAffiliate supportedAffiliate = user.getSupportedAffiliate();
    if (response.isUpdatedArticlesPage(pageable, supportedAffiliate)) {
      articles = tyreArticleReorder.reorderInBatch(
          response.getCurrentTopBunchOfArticlesByPageNr(supportedAffiliate), brands);
      return ArticleFilteringResponseDto.splitArticlePage(articles, pageable,
          response.getAvailabilityCurrentPageNr(), supportedAffiliate);
    }
    articles = response.bindUpdatedArticles(
        findPriceAndAvailabilities(user, response.getNextTopBunchOfArticles(supportedAffiliate)));
    int availabilityCurrentPageNr = 0;
    if (pageable.hasPrevious()) {
      availabilityCurrentPageNr = response.getAvailabilityCurrentPageNr() + 1;
    }
    storeCachedArticlesResult(articles, response.getFilters(), contextKey,
        availabilityCurrentPageNr);

    articles = tyreArticleReorder
        .reorderInBatch(response.getNextTopBunchOfArticles(supportedAffiliate), brands);
    return ArticleFilteringResponseDto.splitArticlePage(articles, pageable,
        availabilityCurrentPageNr, supportedAffiliate);
  }

  private List<ArticleDocDto> findPriceAndAvailabilities(final UserInfo user,
      final List<ArticleDocDto> articles) {
    return ivdsArticleTaskExecutors
        .executeTaskPriceAndAvailabilityWithoutVehicle(user, articles);
  }

}
