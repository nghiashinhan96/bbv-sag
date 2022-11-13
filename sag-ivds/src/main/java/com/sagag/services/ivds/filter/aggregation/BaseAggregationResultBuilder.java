package com.sagag.services.ivds.filter.aggregation;

import com.sagag.services.domain.article.ArticleFilterItem;

import org.apache.commons.collections4.MapUtils;

import java.util.Comparator;
import java.util.Map;

public abstract class BaseAggregationResultBuilder implements AggregationResultBuilder {

  protected static Comparator<ArticleFilterItem> orderCriteriaComparator(
      final Map<String, Integer> ordersMap) {
    return Comparator.comparing(item -> MapUtils.getObject(ordersMap, item.getId(),
        Integer.MAX_VALUE));
  }

  protected static Comparator<ArticleFilterItem> descriptionCriteriaCompartor() {
    return Comparator.comparing(ArticleFilterItem::getDescription, String.CASE_INSENSITIVE_ORDER);
  }

}
