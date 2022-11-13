package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.oils.OilArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.OilArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class OilArticleFilterImpl implements IArticleFilter {

  @Autowired
  private OilArticleSearchServiceImpl oilFiltering;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    final OilArticleSearchCriteria criteria = request.getOilSearchRequest().toCriteria();
    criteria.setAffNameLocks(affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    aggregationFilterMapper().accept(request, criteria);
    return oilFiltering.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.OIL_SEARCH;
  }

}
