package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.wsp.UniversalPartArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.WSPSearchRequest;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class UniversalPartArticleFilterImpl implements IArticleFilter {

  @Autowired
  private UniversalPartArticleSearchServiceImpl universalPartFiltering;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    Assert.notNull(request.getWspSearchRequest(), "The given WSP search request mut not be null");

    final WSPSearchRequest wspRequest = request.getWspSearchRequest();

    final UniversalPartArticleSearchCriteria criteria = wspRequest.toCriteria();
    criteria.setAffNameLocks(affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    aggregationFilterMapper().accept(request, criteria);

    return universalPartFiltering.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.WSP_SEARCH;
  }

}
