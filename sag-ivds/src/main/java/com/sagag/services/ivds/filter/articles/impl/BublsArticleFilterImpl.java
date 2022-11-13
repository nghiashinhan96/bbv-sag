package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.bulbs.BulbArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.BulbArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BublsArticleFilterImpl implements IArticleFilter {

  @Autowired
  private BulbArticleSearchServiceImpl bulbFiltering;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    // Build supplier filter to search criteria builder
    final BulbArticleSearchCriteria bulbCriteria = request.getBulbSearchRequest().toCriteria();
    bulbCriteria.setAffNameLocks(affNameLocks);
    bulbCriteria.setSaleOnBehalf(isSaleOnBehalf);
    aggregationFilterMapper().accept(request, bulbCriteria);
    return bulbFiltering.filter(bulbCriteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.BULB_SEARCH;
  }

}
