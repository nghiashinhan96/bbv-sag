package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.batteries.BatteryArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.BatteryArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BatteryArticleFilterImpl implements IArticleFilter {

  @Autowired
  private BatteryArticleSearchServiceImpl batteryFiltering;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    // Build supplier filter to search criteria builder
    final BatteryArticleSearchCriteria batteryCriteria =
        request.getBatterySearchRequest().toCriteria();
    batteryCriteria.setAffNameLocks(affNameLocks);
    batteryCriteria.setSaleOnBehalf(isSaleOnBehalf);
    aggregationFilterMapper().accept(request, batteryCriteria);
    return batteryFiltering.filter(batteryCriteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.BATTERY_SEARCH;
  }

}
