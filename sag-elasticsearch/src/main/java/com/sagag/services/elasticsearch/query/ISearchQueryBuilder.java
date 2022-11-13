package com.sagag.services.elasticsearch.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

/**
 * Interface to provide search query builder.
 */
@FunctionalInterface
public interface ISearchQueryBuilder<T> {

  /**
   * Builds the Elastisearch query for searching.
   *
   * @param criteria the search criteria
   * @param pageable the pagination
   * @param indices the list of indexes for searching
   * @return the result of {@link org.springframework.data.elasticsearch.core.query.SearchQuery}
   */
  SearchQuery buildQuery(T criteria, Pageable pageable, String... indices);

  /**
   * The default pageable if null.
   *
   * @param pageable the pagination
   * @return the object of {@link org.springframework.data.domain.Pageable}
   */
  default Pageable defaultPageable(Pageable pageable) {
    return pageable == null ? Pageable.unpaged() : pageable;
  }

}
