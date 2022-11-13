package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.tyres.MatchCodeArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.MatchCodeArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class MatchCodeArticleFilterImpl implements IArticleFilter {

  @Autowired
  private MatchCodeArticleSearchServiceImpl matchCodeFiltering;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    final String matchCode = request.getMatchCodeStr();
    final MatchCodeArticleSearchCriteria criteria = new MatchCodeArticleSearchCriteria();
    criteria.setMatchCode(matchCode);
    criteria.setAffNameLocks(affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    aggregationFilterMapper().accept(request, criteria);
    return matchCodeFiltering.filter(criteria, pageable);
  }


  @Override
  public FilterMode mode() {
    return FilterMode.MATCH_CODE;
  }

}
