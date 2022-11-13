package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.article.ArticleIdListSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.ArticleIdListSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.ArticlePartListSearchRequest;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ArticlePartListFilterImpl implements IArticleFilter {

  @Autowired
  private ArticleIdListSearchServiceImpl articleIdListSearchService;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    Assert.notNull(request.getArticlePartListSearchRequest(),
        "The given article part list search request must not be null.");

    final ArticlePartListSearchRequest partListSearchRequest =
        request.getArticlePartListSearchRequest();

    final ArticleIdListSearchCriteria criteria = partListSearchRequest.toCriteria();
    criteria.setAffNameLocks(affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    criteria.setUseDefaultArtId(true);
    aggregationFilterMapper().accept(request, criteria);

    return articleIdListSearchService.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.PART_LIST;
  }

}
