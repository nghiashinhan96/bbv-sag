package com.sagag.services.elasticsearch.api.impl.articles.article;

import com.sagag.services.elasticsearch.api.LoopSearchSupport;
import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.ReferenceAndArtNumSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.query.articles.article.GenArtIdAndReferenceArticleQueryBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GenArtIdAndReferenceArticleSearchServiceImpl
    extends AbstractArticleElasticsearchService
    implements IArticleSearchService<ReferenceAndArtNumSearchCriteria, Page<ArticleDoc>>,
    LoopSearchSupport<ReferenceAndArtNumSearchCriteria> {

  @Autowired
  private GenArtIdAndReferenceArticleQueryBuilder queryBuilder;

  @Override
  public Page<ArticleDoc> search(ReferenceAndArtNumSearchCriteria criteria, Pageable pageable) {
    final SearchQuery[] searchQueries = buildLoopQueries(criteria, pageable);
    return searchPageLoop(searchQueries, ArticleDoc.class);
  }

  @Override
  public ArticleFilteringResponse filter(ReferenceAndArtNumSearchCriteria criteria,
    Pageable pageable) {
    criteria.onAggregated();
    final SearchQuery[] searchQueries = buildLoopQueries(criteria, pageable);
    return filterLoop(searchQueries, filteringExtractor(pageable));
  }

  @Override
  public SearchQuery[] buildLoopQueries(ReferenceAndArtNumSearchCriteria criteria,
    Pageable pageable) {
    List<SearchQuery> queries = new ArrayList<>();
    Optional.ofNullable(queryBuilder.buildQuery(criteria, pageable, index()))
      .ifPresent(queries::add);
    if (criteria.isDisableIgnoreGenArtMatch()) {
      return queries.toArray(new SearchQuery[0]);
    }
    criteria.disableGenArtIdInQuery();
    Optional.ofNullable(queryBuilder.buildQuery(criteria, pageable, index()))
      .ifPresent(queries::add);
    return queries.toArray(new SearchQuery[0]);
  }
}
