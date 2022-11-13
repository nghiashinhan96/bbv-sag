package com.sagag.services.ivds.filter.aggregation;

import com.sagag.services.domain.article.ArticleFilterItem;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Comparator;

public abstract class SubAggregationResultBuilder implements AggregationResultBuilder {

  protected static Comparator<ArticleFilterItem> idNumberCriteriaComparator() {
    return Comparator.comparing(i -> NumberUtils.toDouble(i.getId()));
  }

  protected static Comparator<ArticleFilterItem> idTextCriteriaComparator() {
    return Comparator.comparing(ArticleFilterItem::getId, String.CASE_INSENSITIVE_ORDER);
  }

}
