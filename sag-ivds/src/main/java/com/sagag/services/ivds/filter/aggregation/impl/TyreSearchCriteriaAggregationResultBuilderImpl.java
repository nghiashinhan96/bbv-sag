package com.sagag.services.ivds.filter.aggregation.impl;

import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.ivds.filter.aggregation.BaseAggregationResultBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TyreSearchCriteriaAggregationResultBuilderImpl extends BaseAggregationResultBuilder {

  private static final String SPEED_INDEX_DESC = "speed_index";

  private static final String TYRE_SEGMENT_DESC = "tyre_segment";

  private static final String LOAD_INDEX_DESC = "load_index";

  @AllArgsConstructor
  @Getter
  enum AggregationKind {
    SPEED_INDEX(TyreConstants.SPEED_INDEX_CID, SPEED_INDEX_DESC),
    TYRE_SEGMENT(TyreConstants.TYRE_SEGMENT_CID, TYRE_SEGMENT_DESC),
    LOAD_INDEX(TyreConstants.LOAD_INDEX_CID, LOAD_INDEX_DESC);

    private int key;

    private String description;

    boolean isMatch(Object key) {
      return String.valueOf(this.key).equals(key.toString());
    }

    static String toDescription(Object key) {
      return Stream.of(AggregationKind.values()).filter(t -> t.isMatch(key.toString()))
          .map(AggregationKind::getDescription)
          .findAny().orElse(null);
    }
  }

  @Override
  public void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      Map<String, List<SagBucket>> aggregations) {
    final List<SagBucket> buckets = aggregations.get(ArticleField.CRITERIA_CID.name());
    if (CollectionUtils.isEmpty(buckets)) {
      return;
    }
    final List<SagBucket> targetBuckets = buckets.stream()
        .filter(bucket -> Stream.of(AggregationKind.values()).anyMatch(type -> type.isMatch(bucket.getKey())))
        .collect(Collectors.toList());
    targetBuckets.forEach(buildAggregationResultForCriteriaValue(filters));
  }

  private Consumer<SagBucket> buildAggregationResultForCriteriaValue(Map<String, List<ArticleFilterItem>> filters) {
    return bucket -> {
      if (CollectionUtils.isEmpty(bucket.getSubBuckets())) {
        return;
      }
      final List<SagBucket> buckets = bucket.getSubBuckets().get(0).get(ArticleField.CRITERIA_CVP_RAW.name());
      List<ArticleFilterItem> filterItems =
          buckets.stream().map(this::toArticleFilterItem).collect(Collectors.toList());
      filters.put(AggregationKind.toDescription(bucket.getKey()), filterItems);
    };
  }

  private ArticleFilterItem toArticleFilterItem(SagBucket bucket) {
    return ArticleFilterItem.builder()
        .id(bucket.getKey().toString())
        .amountItem(bucket.getDocCount())
        .description(buildDescription(bucket.getKey().toString(), bucket.getDocCount()))
        .build();
  }

}
