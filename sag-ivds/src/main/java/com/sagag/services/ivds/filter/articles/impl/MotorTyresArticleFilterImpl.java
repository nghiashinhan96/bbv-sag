package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.tyres.MotorArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.MotorTyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.request.filter.MotorTyreArticleSearchRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MotorTyresArticleFilterImpl implements IArticleFilter {

  @Autowired
  private MotorArticleSearchServiceImpl motorTyreFiltering;

  @Autowired
  private MatchCodeArticleFilterImpl  matchCodeFilter;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    Assert.notNull(request.getMotorTyreSearchRequest(),
        "The given motor tyre search request mut not be null");

    final MotorTyreArticleSearchRequest mtRequest = request.getMotorTyreSearchRequest();
    if (mtRequest.isMatchCodeSearch()) {
      return matchCodeFilter.filterArticles(request, pageable, affNameLocks, isSaleOnBehalf);
    }

    final MotorTyreArticleSearchCriteria criteria = mtRequest.toCriteria();
    criteria.setAffNameLocks(affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    aggregationFilterMapper().accept(request, criteria);
    return motorTyreFiltering.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.MOTOR_TYRES_SEARCH;
  }

}
