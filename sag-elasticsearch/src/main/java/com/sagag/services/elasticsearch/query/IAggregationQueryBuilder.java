package com.sagag.services.elasticsearch.query;

import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;

/**
 * Interface for Aggregation script <br>
 *
 * @param <T> ScriptedMetricAggregationBuilder, TopHitsAggregationBuilder,
 *        FiltersAggregationBuilder, ...
 */
public interface IAggregationQueryBuilder<T extends AbstractAggregationBuilder<T>> {

  T build();

}
