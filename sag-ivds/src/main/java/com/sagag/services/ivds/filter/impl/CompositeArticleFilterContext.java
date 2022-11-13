package com.sagag.services.ivds.filter.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.ivds.filter.ArticleFilterContext;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * Class to implement article filtering for searching articles.
 */
@Component
@Slf4j
@Primary
public class CompositeArticleFilterContext extends ArticleFilterContext {

  @Autowired
  private List<ArticleFilterContext> articleFilterContexts;

  /**
   * Returns the filtered articles by search request.
   *
   * @param user the current user login
   * @param request the filtering request
   * @param pageable the paging object
   * @return the result of {@link ArticleFilteringResponseDto}
   */
  public ArticleFilteringResponseDto execute(UserInfo user, ArticleFilterRequest request,
      Pageable pageable, Optional<AdditionalSearchCriteria> additional) {
    Assert.hasText(request.getFilterMode(), "The given filter mode must not be empty");

    // #1137: With filtering tyres mode have some specifics sorting,
    // so separate specific method to implement
    final FilterMode filterMode = FilterMode.valueOf(request.getFilterMode());
    return execute(user, filterMode, request, pageable, additional);
  }

  @Override
  public ArticleFilteringResponseDto execute(UserInfo user, FilterMode filterMode,
      ArticleFilterRequest request, Pageable pageable,
      Optional<AdditionalSearchCriteria> additional) {
    log.debug("Executing filter articles by request = {}, pageable = {}", request, pageable);

    return articleFilterContexts.stream()
        .filter(context -> context.supportMode(filterMode))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No support context with this"))
        .execute(user, filterMode, request, pageable, additional);
  }

  @Override
  public boolean supportMode(FilterMode filterMode) {
    throw new UnsupportedOperationException("Un-support this method");
  }

}
