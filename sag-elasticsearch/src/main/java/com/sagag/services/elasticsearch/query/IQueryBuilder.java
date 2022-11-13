package com.sagag.services.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.Collections;
import java.util.Map;

@FunctionalInterface
public interface IQueryBuilder<T> {

  /**
   * Builds the Elastisearch query for searching.
   *
   * @param criteria the search criteria
   * @return the result of {@link QueryBuilder}
   */
  QueryBuilder build(T criteria);

  default Map<String, Float> attributeBoosts() {
    return Collections.emptyMap();
  }
}
