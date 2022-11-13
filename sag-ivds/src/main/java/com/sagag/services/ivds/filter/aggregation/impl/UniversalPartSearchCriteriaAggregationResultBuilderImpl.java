package com.sagag.services.ivds.filter.aggregation.impl;

import com.sagag.services.common.contants.UniversalPartConstants;
import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.domain.CriteriaTxt;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.hazelcast.api.CriteriaCacheService;
import com.sagag.services.ivds.filter.aggregation.BaseAggregationResultBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class UniversalPartSearchCriteriaAggregationResultBuilderImpl
    extends BaseAggregationResultBuilder {

  @Autowired
  private CriteriaCacheService criteriaCacheService;

  @Override
  public void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      Map<String, List<SagBucket>> aggregations) {
    final List<SagBucket> buckets = aggregations.get(ArticleField.CRITERIA_CID.name());
    if (CollectionUtils.isEmpty(buckets)) {
      return;
    }
    final List<String> criteriaIds = buckets.stream()
        .map(bucket -> Objects.toString(bucket.getKey())).collect(Collectors.toList());
    final Map<String, CriteriaTxt> criteriaMap =
        criteriaCacheService.searchCriteriaByIds(criteriaIds);
    List<SagBucket> criteriaCnList = aggregations.get(ArticleField.CRITERIA_CN.name());

    List<ArticleFilterItem> filterItems = Lists.newArrayList();
    buckets
        .forEach(buildAggregationResultForCriteriaValue(filterItems, criteriaCnList, criteriaMap));
    filters.put(UniversalPartConstants.CRITERION_MAP_NAME, filterItems);
  }

  private Consumer<SagBucket> buildAggregationResultForCriteriaValue(
      List<ArticleFilterItem> filterItems, List<SagBucket> criteriaCn,
      Map<String, CriteriaTxt> criteriaMap) {
    return bucket -> {
      if (CollectionUtils.isEmpty(bucket.getSubBuckets())) {
        return;
      }
      ArticleFilterItem articleFilterItem = toArticleFilterItem(bucket);

      String cid = articleFilterItem.getId();

      if (criteriaMap.containsKey(cid)) {
        articleFilterItem.setDescription(criteriaMap.get(cid).getCndech());
      }
      final List<SagBucket> subBuckets =
          bucket.getSubBuckets().get(0).get(ArticleField.CRITERIA_CVP_RAW.name());

      List<ArticleFilterItem> subFilterItems =
          subBuckets.stream().map(this::toArticleFilterItem).collect(Collectors.toList());

      articleFilterItem.setChildren(subFilterItems);
      articleFilterItem.setHasChildren(CollectionUtils.isNotEmpty(subFilterItems));
      filterItems.add(articleFilterItem);
    };
  }

  private ArticleFilterItem toArticleFilterItem(SagBucket bucket) {
    return ArticleFilterItem.builder().id(bucket.getKey().toString())
        .amountItem(bucket.getDocCount()).description(bucket.getKey().toString()).build();
  }

}
