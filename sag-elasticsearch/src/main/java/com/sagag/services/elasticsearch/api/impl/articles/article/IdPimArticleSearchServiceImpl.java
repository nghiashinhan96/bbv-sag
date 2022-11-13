package com.sagag.services.elasticsearch.api.impl.articles.article;

import com.sagag.services.elasticsearch.api.impl.articles.KeywordArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.query.articles.article.ArticleIdArticleSearchQueryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class IdPimArticleSearchServiceImpl extends KeywordArticleSearchService {

  @Autowired
  private ArticleIdArticleSearchQueryBuilder queryBuilder;

  /**
   * Returns the {@link ArticleDoc} list from its idSagSys.
   *
   * <p>
   * With one idSagSys, the service can return more than one article.
   *
   * @param criteria the searching idSagSys
   * @param pageable the searching page request
   * @return a list of {@link ArticleDoc}
   */
  @Override
  public Page<ArticleDoc> search(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    if (StringUtils.isBlank(criteria.getText())) {
      return Page.empty();
    }
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return searchPage(searchQuery, ArticleDoc.class);
  }

  /**
   * Search article by id_sagsys and aggregation supplier & gaid.
   *
   * @param criteria {@link KeywordArticleSearchCriteria} the article criteria for searching
   * @param pageable the searching page request
   * @return the object response of {@link ArticleFilteringResponse}
   *
   */
  @Override
  public ArticleFilteringResponse filter(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    if (!criteria.hasText()) {
      return ArticleFilteringResponse.empty();
    }
    criteria.onAggregated();
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return search(searchQuery, filteringExtractor(pageable));
  }
}
