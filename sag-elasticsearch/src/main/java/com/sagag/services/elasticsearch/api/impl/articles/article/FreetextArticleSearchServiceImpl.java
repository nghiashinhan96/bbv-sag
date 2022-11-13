package com.sagag.services.elasticsearch.api.impl.articles.article;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.elasticsearch.api.impl.articles.ArticleLoopSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.freetext.FreetextArticleQueryBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class FreetextArticleSearchServiceImpl extends ArticleLoopSearchService {

  @Autowired
  private FreetextArticleQueryBuilder queryBuilder;

  /**
   * Search article by free text and filter with supplier or gaid.
   *
   * @param criteria {@link KeywordArticleSearchCriteria} the article criteria for searching
   * @param pageable the searching page request
   * @return the object response of {@link ArticleFilteringResponse}
   *
   */
  @Override
  public Page<ArticleDoc> search(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    if (!criteria.hasText()) {
      return Page.empty();
    }
    criteria.onUsePartsExt();

    if (criteria.isPerfectMatched()) {
      final SearchQuery[] searchQueries = buildLoopQueries(criteria, pageable);
      return searchPageLoop(searchQueries, ArticleDoc.class);
    }
    return searchArticlesLoop(criteria, pageable, true);
  }

  @LogExecutionTime
  @Override
  public ArticleFilteringResponse filter(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    if (!criteria.hasText()) {
      return ArticleFilteringResponse.empty();
    }
    criteria.onAggregated();
    criteria.onUsePartsExt();

    if (criteria.isPerfectMatched()) {
      final SearchQuery[] searchQueries = buildLoopQueries(criteria, pageable);
      return filterLoop(searchQueries, filteringExtractor(pageable));
    }
    return filterArticlesLoop(criteria, pageable, true);
  }

  @Override
  public ISearchQueryBuilder<KeywordArticleSearchCriteria> queryBuilder() {
    return queryBuilder;
  }
}
