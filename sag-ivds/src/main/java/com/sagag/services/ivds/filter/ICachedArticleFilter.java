package com.sagag.services.ivds.filter;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface to provide some functions for store articles info in cached.
 *
 */
public interface ICachedArticleFilter {

  /**
   * Stores the all articles result and update AX ERP info.
   *
   * @param user the current user login
   * @param response the searching response
   * @param contextKey the generated cached key
   * @param mode the search mode
   * @param promotionBrands promotion brands to re-sorting
   * @param isUseMulitipleLevelAgg use for building criteria aggregation.
   */
  void storeArticlesInCached(UserInfo user, FilteredArticleAndAggregationResponse response,
      String contextKey, FilterMode mode, List<String> promotionBrands,
      boolean isUseMulitipleLevelAgg);

  /**
   * Returns the batch articles result from cached.
   *
   * @param user the current user login
   * @param contextKey the cached key to get tyres result.
   * @param pageable the paging object
   * @param mode the search mode
   * @return the result of {@link ArticleFilteringResponseDto}
   */
  ArticleFilteringResponseDto filterArticlesInCached(UserInfo user, String contextKey,
      Pageable pageable, FilterMode mode, List<String> promotionBrands);

  /**
   * Returns or generate the context key is used to store in hazelcast.
   *
   * @param contextKey
   * @param userKey
   * @return the context key is used.
   */
  default String contextKey(String contextKey, String userKey) {
    // Generate context key if request is empty.
    return StringUtils.defaultIfBlank(contextKey,
        generateSearchContextKey(userKey));
  }

  static String generateSearchContextKey(String userKey) {
    return StringUtils.defaultString(userKey) + System.currentTimeMillis();
  }
}
