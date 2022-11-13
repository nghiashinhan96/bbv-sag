package com.sagag.services.elasticsearch.utils;

import com.sagag.services.elasticsearch.extractor.SagBucket;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Elasticsearch aggregation utility methods.
 */
@UtilityClass
public final class AggregationUtils {

  public static boolean isEmpty(final Map<String, List<SagBucket>> aggregations) {
    return aggregations.values().stream().flatMap(List::stream).distinct()
        .collect(Collectors.toList()).isEmpty();
  }

  public static boolean anyMatch(final Map<String, List<SagBucket>> aggregations, String value) {
    return aggregations.values().stream().flatMap(List::stream)
        .anyMatch(t -> Objects.equals(t.getKey().toString(), value));
  }
}
