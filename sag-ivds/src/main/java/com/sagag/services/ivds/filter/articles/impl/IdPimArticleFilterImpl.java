package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.KeywordArticleSearchService;
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
public class IdPimArticleFilterImpl implements IArticleFilter {

  @Autowired
  @Qualifier("idPimArticleSearchServiceImpl")
  private KeywordArticleSearchService idPimFiltering;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(
        request.getKeyword(), affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    aggregationFilterMapper().accept(request, criteria);
    return idPimFiltering.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.ID_SAGSYS;
  }

}
