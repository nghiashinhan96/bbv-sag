package com.sagag.services.elasticsearch.query;

import com.sagag.services.elasticsearch.enums.IAttributePath;
import com.sagag.services.elasticsearch.utils.ElasticsearchConstants;

import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface to provide aggregation search query builder.
 */
public interface IAggregationBuilder {

  default void buildAggregation(final NativeSearchQueryBuilder queryBuilder){
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @param queryBuilder
   * @param paths
   */
  default void aggregated(NativeSearchQueryBuilder queryBuilder, IAttributePath... paths) {
    if (ArrayUtils.isEmpty(paths) || queryBuilder == null) {
      return;
    }
    Stream.of(paths)
        .map(path -> {
          TermsAggregationBuilder termsAggeBuilder = AggregationBuilders.terms(path.aggTerms())
              .field(path.field())
              .order(Terms.Order.term(true))
              .size(ElasticsearchConstants.MAX_SIZE_AGGS)
              .showTermDocCountError(true);
          if (!path.isNested()) {
            return termsAggeBuilder;
          }
          return AggregationBuilders.nested(path.path(), path.path()).subAggregation(termsAggeBuilder);
        })
        .forEach(queryBuilder::addAggregation);
  }

  /**
   * Add {@link AggregationPathMultiLevel} into {@link NativeSearchQueryBuilder} <br/>
   * Support multiple aggregation level
   *
   * @param queryBuilder
   * @param aggregation
   */
  default void aggregated(final NativeSearchQueryBuilder queryBuilder, final AggregationPathMultiLevel... aggregation) {
    if (ArrayUtils.isEmpty(aggregation) || queryBuilder == null) {
      return;
    }
    Stream.of(aggregation).forEach(item -> {
      if (item == null || queryBuilder == null || Objects.isNull(item.getPath())) {
        return;
      }
      IAttributePath path = item.getPath();
      TermsAggregationBuilder termsAggeBuilder = AggregationBuilders.terms(path.aggTerms())
          .field(path.field())
          .order(Terms.Order.term(true))
          .size(ElasticsearchConstants.MAX_SIZE_AGGS)
          .showTermDocCountError(true);

      Optional.ofNullable(item.getSubPaths())
          .orElseGet(ArrayList::new).stream()
          .forEach(paths -> aggregated(termsAggeBuilder, paths));

      if (!path.isNested()) {
        queryBuilder.addAggregation(termsAggeBuilder);
      } else {
        queryBuilder.addAggregation(
            AggregationBuilders.nested(path.path(), path.path()).subAggregation(termsAggeBuilder));
      }
    });
  }

  default void aggregated(final TermsAggregationBuilder queryBuilder, final AggregationPathMultiLevel paths) {
    if (paths == null || queryBuilder == null || paths.getPath() == null) {
      return;
    }
    IAttributePath path = paths.getPath();
    TermsAggregationBuilder termsAggeBuilder = AggregationBuilders.terms(path.aggTerms())
        .field(path.field())
        .order(Terms.Order.term(true))
        .size(ElasticsearchConstants.MAX_SIZE_AGGS)
        .showTermDocCountError(true);

    Optional.ofNullable(paths.getSubPaths())
        .orElseGet(ArrayList::new).stream()
        .forEach(item -> aggregated(termsAggeBuilder, item));

    if(!path.isNested()) {
      queryBuilder.subAggregation(termsAggeBuilder);
    } else {
      queryBuilder.subAggregation(
          AggregationBuilders.nested(path.path(), path.path()).subAggregation(termsAggeBuilder));
    }
  }
}
