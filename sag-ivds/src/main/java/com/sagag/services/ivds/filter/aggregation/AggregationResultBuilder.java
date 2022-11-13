package com.sagag.services.ivds.filter.aggregation;

import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.extractor.SagBucket;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@FunctionalInterface
public interface AggregationResultBuilder {



  /**
   * Builds the aggregation result.
   *
   * @param filters
   * @param aggregations
   */
  void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      Map<String, List<SagBucket>> aggregations);

  default Function<List<SagBucket>, List<ArticleFilterItem>> articleFilterConverter(
      final Function<SagBucket, ArticleFilterItem> itemMapper) {
    return buckets -> {
      if (CollectionUtils.isEmpty(buckets)) {
        return Collections.emptyList();
      }
      // Sort by ascending alphabetical order.
      return buckets.stream().map(itemMapper)
          .sorted(sortAscByDescription())
          .collect(Collectors.toList());
    };
  }

  static Comparator<ArticleFilterItem> sortAscByDescription() {
    return (item1, item2) -> String.CASE_INSENSITIVE_ORDER.compare(item1.getDescription(),
        item2.getDescription());
  }

  default String buildDescription(final String str, final long count) {
    return str + StringUtils.SPACE + '(' + count + ')';
  }

  default List<ArticleFilterItem> getDefaultFilterItems(final List<SagBucket> sagBuckets) {

    if (CollectionUtils.isEmpty(sagBuckets)) {
      return Collections.emptyList();
    }

    // Update supplier article description
    // Sort by ascending alphabetical order.
    return sagBuckets.stream().map(bucket -> {
      final String text = StringUtils
          .upperCase(StringUtils.defaultIfBlank(bucket.getKey().toString(), StringUtils.EMPTY));
      return ArticleFilterItem.builder().id(bucket.getKey().toString())
          .amountItem(bucket.getDocCount())
          .description(buildDescription(text, bucket.getDocCount())).build();
    }).sorted(sortAscByDescription()).collect(Collectors.toList());
  }

}
