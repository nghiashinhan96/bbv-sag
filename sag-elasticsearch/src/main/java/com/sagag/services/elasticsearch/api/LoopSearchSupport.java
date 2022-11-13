package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

public interface LoopSearchSupport<T> {

  /**
   * Returns the array of search queries by criteria.
   *
   * @param criteria the search criteria
   * @param pageable the pagination
   * @return the array of search queries
   */
  SearchQuery[] buildLoopQueries(T criteria, Pageable pageable);

  /**
   * Returns the search query builder to generate Elasticsearch query.
   * @return the implementation of search query builder
   */
  default ISearchQueryBuilder<T> queryBuilder() {
    return null;
  }

}
