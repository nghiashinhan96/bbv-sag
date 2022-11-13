package com.sagag.services.ivds.filter.aggregation.impl;

import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.domain.CriteriaTxt;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.hazelcast.api.CriteriaCacheService;
import com.sagag.services.ivds.filter.aggregation.SubAggregationResultBuilder;
import com.sagag.services.ivds.filter.aggregation.SubAggregationResultUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CriteriaAggregationResultBuilderImpl extends SubAggregationResultBuilder {

  private static final int MINIMUM_CRITERIA_VALUE = 1;

  public static final String AGGS_NAME = "criterias";

  @Autowired
  private CriteriaCacheService criteriaCacheService;

  @Autowired
  @Qualifier("criteriaValueAggregationResultBuilderImpl")
  private SubAggregationResultBuilder criteriaValueAggregationResultBuilder;

  @Override
  public void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      Map<String, List<SagBucket>> aggregations) {
    final List<SagBucket> buckets = aggregations.get(ArticleField.CRITERIA_CID.name());
    if (CollectionUtils.isEmpty(buckets)) {
      return;
    }
    final List<String> cids = buckets.stream().map(bucket -> bucket.getKey().toString())
        .collect(Collectors.toList());

    final Map<String, CriteriaTxt> criteriaTexts = criteriaCacheService.searchCriteriaByIds(cids);
    final List<ArticleFilterItem> filterItems = buckets.stream()
        .map(bucket -> {
          ArticleFilterItem subArticleFilterItem = new ArticleFilterItem();
          subArticleFilterItem.setId(bucket.getKey().toString());
          subArticleFilterItem.setAmountItem(bucket.getDocCount());
          subArticleFilterItem.setDescription(defaultCriteriaDescription(criteriaTexts, bucket));
          subArticleFilterItem.setParentId(subArticleFilterItem.getId());
          subArticleFilterItem.setType("criteria");
          subArticleFilterItem.setSelected(false);
          subArticleFilterItem.setShown(true);
          subArticleFilterItem.setUuid(UUID.randomUUID().toString());

          Map<String, List<ArticleFilterItem>> articleFilterMap = new HashMap<>();
          bucket.getSubBuckets().stream()
          .forEach(subBuk -> criteriaValueAggregationResultBuilder.buildAggregationResult(
              articleFilterMap, subBuk));
          subArticleFilterItem.setSubFilters(articleFilterMap);

          final List<ArticleFilterItem> subFilterItems =
              articleFilterMap.get(CriteriaValueAggregationResultBuilderImpl.AGGS_NAME);
          subArticleFilterItem.setChildren(
              SubAggregationResultUtils.addShowMoreItem(subFilterItems));
          subArticleFilterItem.setHasChildren(
              !CollectionUtils.isEmpty(subArticleFilterItem.getChildren()));
          return subArticleFilterItem;
        }).collect(Collectors.toList());

    filterItems
        .removeIf(filter -> CollectionUtils.size(filter.getChildren()) <= MINIMUM_CRITERIA_VALUE);
    filters.putIfAbsent(AGGS_NAME, filterItems);
  }

  private String defaultCriteriaDescription(Map<String, CriteriaTxt> criteriaTexts,
      SagBucket bucket) {
    CriteriaTxt cTxt = criteriaTexts.getOrDefault(bucket.getKey().toString(), new CriteriaTxt());
    return buildDescription(StringUtils.defaultString(cTxt.getCndech()), bucket.getDocCount());
  }

}
