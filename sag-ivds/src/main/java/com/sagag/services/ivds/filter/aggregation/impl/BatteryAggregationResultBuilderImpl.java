package com.sagag.services.ivds.filter.aggregation.impl;

import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.common.BatteryConstants;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.ivds.filter.aggregation.BaseAggregationResultBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BatteryAggregationResultBuilderImpl extends BaseAggregationResultBuilder {

  @Override
  public void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      Map<String, List<SagBucket>> aggregations) {
    List<SagBucket> ampereAggregations = aggregations.get(BatteryConstants.AMPERE_HOUR_MAP_NAME);
    if(CollectionUtils.isEmpty(ampereAggregations)) {
      return;
    }
    // Get ampere_hours filter list
    filters.put(BatteryConstants.AMPERE_HOUR_MAP_NAME, getDefaultFilterItems(ampereAggregations));
  }

}
