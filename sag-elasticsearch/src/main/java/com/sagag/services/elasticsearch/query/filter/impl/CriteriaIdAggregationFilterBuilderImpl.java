package com.sagag.services.elasticsearch.query.filter.impl;

import com.sagag.services.elasticsearch.criteria.article.ArticleAggregateCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.filter.AggregationFilterBuilder;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class CriteriaIdAggregationFilterBuilderImpl implements AggregationFilterBuilder {

  @Override
  public void addFilter(BoolQueryBuilder searchQueryBuilder, ArticleAggregateCriteria aggCriteria) {
    addFilter(searchQueryBuilder, ArticleField.CRITERIA_CID, aggCriteria.getCids());
  }

}
