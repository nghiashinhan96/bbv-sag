package com.sagag.services.ivds.filter.aggregation.impl;

import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.common.OilConstants;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.ivds.filter.aggregation.BaseAggregationResultBuilder;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class OilAggregationResultBuilderImpl extends BaseAggregationResultBuilder {

  private static final List<String> OIL_NAMES = Arrays.asList(OilConstants.VISCOSITY_MAP_NAME,
      OilConstants.VISCOSITY_MAP_NAME);

  @Override
  public void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      Map<String, List<SagBucket>> aggregations) {
    aggregations.keySet().stream().filter(OIL_NAMES::contains)
        .forEach(item -> filters.put(item, getDefaultFilterItems(aggregations.get(item))));
  }

}
