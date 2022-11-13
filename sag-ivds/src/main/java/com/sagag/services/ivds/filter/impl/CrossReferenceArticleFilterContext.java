package com.sagag.services.ivds.filter.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.filter.ArticleFilterContext;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Order(5)
public class CrossReferenceArticleFilterContext extends ArticleFilterContext {

  @Override
  public ArticleFilteringResponseDto execute(UserInfo user, FilterMode filterMode,
      ArticleFilterRequest request,
      Pageable pageable, Optional<AdditionalSearchCriteria> additional) {
    log.debug("Returns the article cross-reference search result by request = {}", request);

    FilteredArticleAndAggregationResponse esResponse = getArticleFilterResponse(user,
        filterMode, request, pageable);

    // Brand sorting
    Page<ArticleDocDto> artPage = Page.empty();
    if (esResponse.hasArticles()) {
      List<ArticleDocDto> articlesWithStock = findAndSortStock(user, esResponse.getArticles(),
          Optional.of(AdditionalSearchCriteria.builder()
              .isExcludeSubArticles(!filterMode.isTyres()).build()));
      artPage = new PageImpl<>(sortArticlesBrandPriority(user, articlesWithStock));
    }

    // Build the response without filter
    return ArticleFilteringResponseDto.builder().articles(artPage).build();
  }

  @Override
  public boolean supportMode(FilterMode filterMode) {
    return filterMode.isCrossReferenceSearch();
  }

}
