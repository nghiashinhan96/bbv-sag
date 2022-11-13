package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.ArticleLoopSearchService;
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
public class ArticleNumberFilterImpl implements IArticleFilter {

  @Autowired
  @Qualifier("OEAndArtNumArticleSearchServiceImpl")
  private ArticleLoopSearchService oeAndArtNumArticleFiltering;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(
        request.getKeyword(), affNameLocks);
    aggregationFilterMapper().accept(request, criteria);

    // Set perfect match value
    criteria.setPerfectMatched(request.isPerfectMatched());
    criteria.setPreferSagsysIds(request.getPreferSagsysIds());

    // Set direct match value
    criteria.setDirectMatch(request.isDirectMatch());
    criteria.setSearchExternal(!request.isPerfectMatched() && !request.isHasPreviousData());

    criteria.setSaleOnBehalf(isSaleOnBehalf);
    return oeAndArtNumArticleFiltering.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.ARTICLE_NUMBER;
  }

}
