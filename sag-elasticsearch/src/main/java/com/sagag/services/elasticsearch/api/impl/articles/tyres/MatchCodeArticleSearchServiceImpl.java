package com.sagag.services.elasticsearch.api.impl.articles.tyres;

import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.MatchCodeArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.TyreArticleResponse;
import com.sagag.services.elasticsearch.query.articles.tyres.MatchCodeTyreArticleQueryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class MatchCodeArticleSearchServiceImpl extends AbstractArticleElasticsearchService
    implements IArticleSearchService<MatchCodeArticleSearchCriteria, TyreArticleResponse> {

  @Autowired
  private MatchCodeTyreArticleQueryBuilder queryBuilder;

  /**
   * Returns the tyre articles information by match_code.
   *
   * @param criteria the match code input
   * @param pageable the paging request
   * @return the {@link TyreArticleResponse}, nullable
   */
  @Override
  public TyreArticleResponse search(MatchCodeArticleSearchCriteria criteria, Pageable pageable) {
    if (criteria == null || StringUtils.isBlank(criteria.getMatchCode())) {
      return TyreArticleResponse.empty();
    }
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return search(searchQuery, TyreArticleResultsExtractors.extractTyres(pageable));
  }

  @Override
  public ArticleFilteringResponse filter(MatchCodeArticleSearchCriteria criteria,
      Pageable pageable) {
    if (criteria == null || StringUtils.isBlank(criteria.getMatchCode())) {
      return ArticleFilteringResponse.empty();
    }
    criteria.onAggregated();
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return search(searchQuery, filteringExtractor(pageable));
  }
}
