package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.CrossReferenceArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class CrossReferenceArticleFilterImpl implements IArticleFilter {

  @Autowired
  private IArticleSearchService<CrossReferenceArticleSearchCriteria, Page<ArticleDoc>> crossReferenceFiltering;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable, String[] affNameLocks,
      boolean isSaleOnBehalf) {
    final CrossReferenceArticleSearchCriteria criteria = new CrossReferenceArticleSearchCriteria();
    criteria.setArtNr(request.getCrossReferenceRequest().getArticleNumber());
    criteria.setBrandId(request.getCrossReferenceRequest().getBrandId());
    return crossReferenceFiltering.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.CROSS_REFERENCE;
  }
}
