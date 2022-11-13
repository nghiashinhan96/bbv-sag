package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.article.BarcodeArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BarcodeFilterImpl implements IArticleFilter {

  @Autowired
  private BarcodeArticleSearchServiceImpl barcodeArticleSearchServiceImpl;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(
        request.getKeyword(), affNameLocks);
    aggregationFilterMapper().accept(request, criteria);
    criteria.setPerfectMatched(request.isPerfectMatched());
    criteria.setPreferSagsysIds(request.getPreferSagsysIds());
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    return barcodeArticleSearchServiceImpl.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.BAR_CODE;
  }

}
