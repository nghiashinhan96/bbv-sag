package com.sagag.services.elasticsearch.api.impl.articles.article;

import com.sagag.services.elasticsearch.api.impl.articles.KeywordArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.query.articles.article.parts.EanArticleSearchQueryBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class EanArticleSearchServiceImpl extends KeywordArticleSearchService {

  @Autowired
  private EanArticleSearchQueryBuilder queryBuilder;

  /**
   * Returns the articles in a list of part number by EAN part type and the part number.
   *
   * @param criteria the part number.
   * @return the list of {@link ArticleDoc} with pageable
   */
  @Override
  public Page<ArticleDoc> search(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    if (!criteria.hasText()) {
      return Page.empty();
    }
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return searchPage(searchQuery, ArticleDoc.class);
  }

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