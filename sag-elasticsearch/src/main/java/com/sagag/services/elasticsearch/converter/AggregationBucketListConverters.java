package com.sagag.services.elasticsearch.converter;

import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.elasticsearch.extractor.SagBucket.SagBucketBuilder;

import lombok.experimental.UtilityClass;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class AggregationBucketListConverters {

  public static BiFunction<String, Aggregation, Map<String, List<SagBucket>>> aggregationConverter() {
    return (name, aggregation) -> {
      final Map<String, List<SagBucket>> aggregations = new HashMap<>();

      List<SagBucket> buckets = new ArrayList<>();
      if (aggregation instanceof MultiBucketsAggregation) {
        buckets = toAggregationBuckets((MultiBucketsAggregation) aggregation);
        aggregations.put(name, buckets);
      } else if (aggregation instanceof InternalNested) {
        aggregations.putAll(asInternalNestedMap((InternalNested) aggregation));
      }
      return aggregations;
    };
  }

  private static List<SagBucket> toAggregationBuckets(final MultiBucketsAggregation aggregation) {
    return aggregation.getBuckets().stream().map(toSagBucket()).collect(Collectors.toList());
  }

  private static Map<String, List<SagBucket>> asInternalNestedMap(final InternalNested aggregation) {
    Map<String, List<SagBucket>> subAggsMap = new HashMap<>();
    aggregation.getAggregations().asMap().entrySet()
    .forEach(entry -> subAggsMap.putAll(aggregationConverter().apply(entry.getKey(), entry.getValue())));
    return subAggsMap;
  }

  private static Function<Bucket, SagBucket> toSagBucket() {
    return entry -> {
      SagBucketBuilder bucket = SagBucket.builder()
          .key(entry.getKey())
          .docCount(entry.getDocCount());
      if (Objects.isNull(entry.getAggregations())) {
        return bucket.build();
      }
      return bucket
          .subBuckets(getSubBucket(entry.getAggregations()))
          .build();
    };
  }

  private static List<Map<String, List<SagBucket>>> getSubBucket(Aggregations aggs) {
    if (aggs.asList().stream().anyMatch(MultiBucketsAggregation.class::isInstance)) {
      return aggs.asList().stream().map(MultiBucketsAggregation.class::cast)
          .map(item -> aggregationConverter().apply(item.getName(), item))
          .collect(Collectors.toList());
    } else if (aggs.asList().stream().anyMatch(InternalNested.class::isInstance)) {
      return aggs.asList().stream().map(InternalNested.class::cast)
          .map(AggregationBucketListConverters::asInternalNestedMap)
          .collect(Collectors.toList());
    }
    return new ArrayList<>();
  }
}
