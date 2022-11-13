package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.ArticleLoopSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class FreetextArticleFilterImpl implements IArticleFilter {

  @Autowired
  @Qualifier("freetextArticleSearchServiceImpl")
  private ArticleLoopSearchService freetextFiltering;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(request.getKeyword(), affNameLocks);

    // Enable multilevel aggregation
    criteria.setAggregateMultiLevels(request.getGaIdsMultiLevels());
    // Set perfect match value
    criteria.setPerfectMatched(request.isPerfectMatched());
    criteria.setDirectMatch(request.isDirectMatch());
    criteria.setPreferSagsysIds(request.getPreferSagsysIds());
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    criteria.setSearchExternal(!request.isPerfectMatched() && !request.isHasPreviousData());

    aggregationFilterMapper().accept(request, criteria);
    return freetextFiltering.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.FREE_TEXT;
  }

}
