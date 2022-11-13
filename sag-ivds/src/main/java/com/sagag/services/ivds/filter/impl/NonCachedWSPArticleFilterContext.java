package com.sagag.services.ivds.filter.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.filter.ArticleFilterContext;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Order(2)
public class NonCachedWSPArticleFilterContext extends ArticleFilterContext {

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Override
  public ArticleFilteringResponseDto execute(UserInfo user, FilterMode filterMode,
      ArticleFilterRequest request, Pageable pageable,
      Optional<AdditionalSearchCriteria> additional) {
    final FilteredArticleAndAggregationResponse response =
        getArticleFilterResponse(user, filterMode, request, pageable);
    final Page<ArticleDocDto> articles = response.getArticles();

    List<ArticleDocDto> result = ivdsArticleTaskExecutors.executeTaskWithErpArticle(user,
        articles.getContent(), Optional.empty(), additional);

    return ofResult(new PageImpl<>(result, pageable, articles.getTotalElements()),
        buildArticleFilterItems(response.getAggregations(), filterMode));
  }

  @Override
  public boolean supportMode(FilterMode filterMode) {
    return filterMode.isWSPSearch();
  }

}
