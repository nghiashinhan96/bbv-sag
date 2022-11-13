package com.sagag.services.ivds.filter.aggregation.impl;

import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.ivds.filter.aggregation.SubAggregationResultBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CriteriaValueAggregationResultBuilderImpl extends SubAggregationResultBuilder {

  protected static final String AGGS_NAME = "criterias_value";

  @Override
  public void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      Map<String, List<SagBucket>> aggregations) {
    final List<SagBucket> buckets = aggregations.get(ArticleField.CRITERIA_CVP_RAW.name());
    if (CollectionUtils.isEmpty(buckets)) {
      return;
    }
    final List<ArticleFilterItem> filterItems = buckets.stream()
        .map(this::toArtcleFilterItem)
        .collect(Collectors.toList());

    sortFilter(filterItems);

    filters.putIfAbsent(AGGS_NAME, filterItems);
  }

  private void sortFilter(final List<ArticleFilterItem> filterItems) {
    Comparator<ArticleFilterItem> comparator = filterItems.stream().map(ArticleFilterItem::getId)
        .filter(id -> !NumberUtils.isNumber(id))
        .findAny()
        .map(id -> idTextCriteriaComparator())
        .orElseGet(SubAggregationResultBuilder::idNumberCriteriaComparator);
    filterItems.sort(comparator);
  }

  private ArticleFilterItem toArtcleFilterItem(SagBucket bucket) {
    ArticleFilterItem item = new ArticleFilterItem();
    item.setId(bucket.getKey().toString());
    item.setAmountItem(bucket.getDocCount());
    item.setDescription(buildDescription(bucket.getKey().toString(), bucket.getDocCount()));
    item.setType("criteriaValue");
    item.setSelected(false);
    item.setShown(true);
    item.setUuid(UUID.randomUUID().toString());
    return item;
  }
}
