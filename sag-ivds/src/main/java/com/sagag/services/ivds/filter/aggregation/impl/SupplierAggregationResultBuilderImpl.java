package com.sagag.services.ivds.filter.aggregation.impl;

import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.ivds.filter.aggregation.BaseAggregationResultBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class SupplierAggregationResultBuilderImpl extends BaseAggregationResultBuilder {

  @Override
  public void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      Map<String, List<SagBucket>> aggregations) {
    // Get suppliers filter list
    final List<SagBucket> supplierSagBuckets = aggregations.get(ArticleField.SUPPLIER_RAW.name());
    if(CollectionUtils.isEmpty(supplierSagBuckets)) {
      return;
    }
    filters.put("suppliers", articleFilterConverter(
        supplierArticleFilterItemMapper()).apply(supplierSagBuckets));
  }

  private Function<SagBucket, ArticleFilterItem> supplierArticleFilterItemMapper() {
    return bucket -> {
      final String text = StringUtils
          .upperCase(StringUtils.defaultIfBlank(bucket.getKey().toString(), StringUtils.EMPTY));
      return ArticleFilterItem.builder().id(bucket.getKey().toString())
          .amountItem(bucket.getDocCount())
          .description(buildDescription(text, bucket.getDocCount())).build();
    };
  }
}
