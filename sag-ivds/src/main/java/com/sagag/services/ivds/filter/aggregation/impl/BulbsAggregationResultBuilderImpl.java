package com.sagag.services.ivds.filter.aggregation.impl;

import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.common.BulbConstants;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.ivds.filter.aggregation.BaseAggregationResultBuilder;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class BulbsAggregationResultBuilderImpl extends BaseAggregationResultBuilder {

  private static final List<String> BULB_NAME = Arrays.asList(BulbConstants.VOLTAGE_MAP_NAME,
      BulbConstants.WATT_MAP_NAME, BulbConstants.CODE_MAP_NAME);

  @Override
  public void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      Map<String, List<SagBucket>> aggregations) {
    aggregations.keySet().stream().filter(BULB_NAME::contains)
        .forEach(item -> filters.put(item, getDefaultFilterItems(aggregations.get(item))));
  }

}
