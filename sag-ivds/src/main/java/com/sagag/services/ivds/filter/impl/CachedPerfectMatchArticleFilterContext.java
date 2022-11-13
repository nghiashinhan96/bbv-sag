package com.sagag.services.ivds.filter.impl;

import com.google.common.collect.Lists;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.enums.RelevanceGroupType;
import com.sagag.services.common.profiles.DisableForProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.optimizer.PerfectMatchArticleSearchOptimizer;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.utils.RelevanceArticlesUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
@Slf4j
@Order(1)
@DisableForProfile("country-autonet")
public class CachedPerfectMatchArticleFilterContext extends CachedSpecialShopArticleFilterContext {

  /** Empty Brand Promotion as disable this feature */
  private static final String[] PROMOTION_BRANDS = ArrayUtils.EMPTY_STRING_ARRAY;

  @Autowired
  private PerfectMatchArticleSearchOptimizer perfectMatchArtSearchOptimizer;

  @Override
  public ArticleFilteringResponseDto execute(UserInfo user, FilterMode filterMode,
      ArticleFilterRequest request, Pageable pageable,
      Optional<AdditionalSearchCriteria> additional) {
    log.debug("Returns the perfect match article search result by request = {}", request);

    BiFunction<ArticleFilterRequest, Pageable, FilteredArticleAndAggregationResponse> filterFunc =
        (r, p) -> getArticleFilterResponse(user, filterMode, r, p);

    final List<String> promotionBrands = Lists.newArrayList(PROMOTION_BRANDS);

    // Generate context key if request is empty.
    String contextKey = request.getContextKey();
    if (!pageable.hasPrevious() && StringUtils.isBlank(contextKey)) {
      contextKey = contextKey(request.getContextKey(), user.key());
      contextKey = filterAndStoreArticlesPerfectMatchInCached(filterFunc, user, request, filterMode,
          contextKey, promotionBrands, pageable);
    }

    final ArticleFilteringResponseDto allArtFilteringRes = filterArticlesInCached(
        user, contextKey, pageable, filterMode, promotionBrands);

    final List<ArticleDocDto> cachedArticles = Lists.newArrayList();
    if (allArtFilteringRes != null && allArtFilteringRes.hasContent()) {
      cachedArticles.addAll(allArtFilteringRes.getArticles().getContent());
      request.setHasPreviousData(true);
    }

    // Update additional info
    if (!filterMode.isTyres()) {
      additional.ifPresent(add -> add.setIsExcludeSubArticles(Boolean.TRUE));
    }

    final FilteredArticleAndAggregationResponse filteredArtAndAggRes =
        perfectMatchArtSearchOptimizer
            .filterOptimized(user, cachedArticles, filterFunc, request, pageable, additional);

    Map<String, List<ArticleFilterItem>> filterItems =
        buildArticleFilterItems(filteredArtAndAggRes.getAggregations(), filterMode,
            request.isUseMultipleLevelAggregation());
    if (filterItems.isEmpty() && Objects.nonNull(allArtFilteringRes)) {
      filterItems = allArtFilteringRes.getFilters();
    }

    return ofResult(filteredArtAndAggRes.getArticles(), filterItems, contextKey);
  }

  private String filterAndStoreArticlesPerfectMatchInCached(
      BiFunction<ArticleFilterRequest, Pageable, FilteredArticleAndAggregationResponse> filterFnc,
      UserInfo user, ArticleFilterRequest request, FilterMode filterMode, String contextKey,
      List<String> promotionBrands, Pageable pageable) {

    // Execute direct matches
    request.setDirectMatch(true);
    ArticleFilterRequest directMatchRequest =
        RelevanceArticlesUtils.prepareDirectMatchRequest(request);
    final FilteredArticleAndAggregationResponse directMatches =
        perfectMatchArtSearchOptimizer.filterAllOptimized(filterFnc, directMatchRequest);
    request.setDirectMatch(false);
    directMatches.getArticles()
        .forEach(article -> article.setRelevanceGroupType(RelevanceGroupType.DIRECT_MATCH));

    // Execute reference matches
    request.setHasPreviousData(directMatches.hasArticles());
    final FilteredArticleAndAggregationResponse referenceMatches =
        perfectMatchArtSearchOptimizer.filterAllOptimized(filterFnc, request);
    referenceMatches.getArticles()
        .forEach(article -> article.setRelevanceGroupType(RelevanceGroupType.REFERENCE_MATCH));

    List<ArticleDocDto> articles = searchAndCombineExternalParts(user, filterMode, request,
        pageable, directMatches, referenceMatches);

    Map<String, List<SagBucket>> aggregations = Objects.nonNull(referenceMatches.getAggregations())
        && !referenceMatches.getAggregations().isEmpty()
        ? referenceMatches.getAggregations() : directMatches.getAggregations();

    List<ArticleDocDto> articlesWithStock = findAndSortStock(user, new PageImpl<>(articles),
        Optional.of(AdditionalSearchCriteria.builder()
            .isExcludeSubArticles(!filterMode.isTyres()).build()));

    Page<ArticleDocDto> artPage = request.isShoppingList()
        ? new PageImpl<>(sortArticleStockDesc(articlesWithStock))
        : new PageImpl<>(sortArticlesBrandPriority(user, articlesWithStock));
    referenceMatches.setArticles(artPage);
    referenceMatches.setAggregations(aggregations);

    if (!referenceMatches.hasContent()) {
      log.debug("Disabling store cache with wildcard keyword or empty result.");
      return StringUtils.EMPTY;
    }

    storeArticlesInCached(user, referenceMatches, contextKey, filterMode,
        promotionBrands, request.isUseMultipleLevelAggregation());
    return contextKey;
  }

  @Override
  public boolean supportMode(FilterMode filterMode) {
    return filterMode.isStoredInCachedPerfectMatch();
  }

  @Override
  public void storeArticlesInCached(UserInfo user, FilteredArticleAndAggregationResponse response,
      String contextKey, FilterMode mode, List<String> promotionBrands,
      boolean isUseMultipleLevelAgg) {
    final List<ArticleDocDto> articles = response.getArticles().getContent();
    storeCachedArticlesResultWithNoRequestAvail(articles, response.getAggregations(), mode,
        contextKey, isUseMultipleLevelAgg);
  }

  @Override
  public ArticleFilteringResponseDto filterArticlesInCached(UserInfo user, String contextKey,
      Pageable pageable, FilterMode mode, List<String> promotionBrands) {
    if (StringUtils.isBlank(contextKey)) {
      return null;
    }
    return getCachedAllArticleResult(contextKey);
  }

}
