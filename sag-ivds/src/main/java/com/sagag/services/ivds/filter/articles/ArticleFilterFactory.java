package com.sagag.services.ivds.filter.articles;

import com.sagag.services.ivds.filter.aggregation.BaseAggregationResultBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ArticleFilterFactory {

  @Autowired
  private List<IArticleFilter> articleFilters;

  @Autowired
  @Qualifier("batteryAggregationResultBuilderImpl")
  private BaseAggregationResultBuilder batteryBuilder;

  @Autowired
  @Qualifier("bulbsAggregationResultBuilderImpl")
  private BaseAggregationResultBuilder bulbsBuilder;

  @Autowired
  @Qualifier("genArtAggregationResultBuilderImpl")
  private BaseAggregationResultBuilder genArtBuilder;

  @Autowired
  @Qualifier("oilAggregationResultBuilderImpl")
  private BaseAggregationResultBuilder oilBuilder;

  @Autowired
  @Qualifier("supplierAggregationResultBuilderImpl")
  private BaseAggregationResultBuilder supplierBuilder;

  @Autowired
  @Qualifier("tyreSearchCriteriaAggregationResultBuilderImpl")
  private BaseAggregationResultBuilder tyreCriteriaBuilder;

  @Autowired
  @Qualifier("universalPartSearchCriteriaAggregationResultBuilderImpl")
  private BaseAggregationResultBuilder wspCriteriaBuilder;

  /**
   * Return the article filter implementation by filter mode.
   *
   * @param filterMode the filter mode
   * @return the implementation
   */
  public IArticleFilter getArticleFilter(FilterMode filterMode) {
    return articleFilters.stream()
        .filter(filter -> filterMode == filter.mode())
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("No support filter with mode = %s", filterMode)));
  }

  public List<BaseAggregationResultBuilder> getAggregationResultBuilder(FilterMode mode) {
    if (mode.isTyres()) {
      return Arrays.asList(tyreCriteriaBuilder, genArtBuilder, supplierBuilder);
    }
    if (mode.isWSPSearch()) {
      return Arrays.asList(wspCriteriaBuilder, genArtBuilder, supplierBuilder);
    }
    return Arrays.asList(genArtBuilder, supplierBuilder, batteryBuilder, bulbsBuilder, oilBuilder);
  }

}
