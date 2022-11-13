package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.tyres.TyreArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.TyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.request.filter.TyreArticleSearchRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class TyresArticleFilterImpl implements IArticleFilter {

  @Autowired
  private TyreArticleSearchServiceImpl tyreFiltering;

  @Autowired
  private MatchCodeArticleFilterImpl  matchCodeFilter;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    Assert.notNull(request.getTyreSearchRequest(),
        "The given tyre search request mut not be null");

    final TyreArticleSearchRequest tRequest = request.getTyreSearchRequest();
    if (tRequest.isMatchCodeSearch()) {
      return matchCodeFilter.filterArticles(request, pageable, affNameLocks, isSaleOnBehalf);
    }

    final TyreArticleSearchCriteria criteria = tRequest.toCriteria();
    criteria.setAffNameLocks(affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    aggregationFilterMapper().accept(request, criteria);
    return tyreFiltering.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.TYRES_SEARCH;
  }

}
