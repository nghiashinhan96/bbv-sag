package com.sagag.services.elasticsearch.query.filter.impl;

import com.sagag.services.elasticsearch.criteria.article.ArticleAggregateCriteria;
import com.sagag.services.elasticsearch.criteria.article.ArticleAggregateMultiLevel;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.filter.AggregationFilterBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class GenArtAggregationMultipleFilterBuilderImpl implements AggregationFilterBuilder {

  @Override
  public void addFilter(BoolQueryBuilder searchQueryBuilder, ArticleAggregateCriteria aggCriteria) {
    if (searchQueryBuilder == null || aggCriteria == null
        || CollectionUtils.isEmpty(aggCriteria.getAggregateMultiLevels())) {
      return;
    }
    searchQueryBuilder.must(buildMainQuery(aggCriteria.getAggregateMultiLevels()));
  }

  private BoolQueryBuilder buildMainQuery(List<ArticleAggregateMultiLevel> aggregateInput) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    aggregateInput.stream().map(this::addFilterForGentArt).forEach(queryBuilder::should);
    return queryBuilder;
  }

  private BoolQueryBuilder addFilterForGentArt(ArticleAggregateMultiLevel item) {
    final BoolQueryBuilder boolQueryBuilderGentArt = QueryBuilders.boolQuery();
    addFilter(boolQueryBuilderGentArt, ArticleField.GA_ID, Arrays.asList(item.getId()));
    if (CollectionUtils.isNotEmpty(item.getSubIds())) {
      final BoolQueryBuilder boolQueryCriteriaId = QueryBuilders.boolQuery();
      item.getSubIds().stream()
          .map(this::addFilterForCriteriaId)
          .forEach(boolQueryCriteriaId::must);
      boolQueryBuilderGentArt.must(boolQueryCriteriaId);
    }
    return boolQueryBuilderGentArt;
  }

  private BoolQueryBuilder addFilterForCriteriaId(ArticleAggregateMultiLevel aggregate) {
    final BoolQueryBuilder bool = QueryBuilders.boolQuery();
    final TermsQueryBuilder queryCid =
        QueryBuilders.termsQuery(ArticleField.CRITERIA_CID.value(), aggregate.getId());
    if (CollectionUtils.isEmpty(aggregate.getSubIds())) {
      return bool.should(QueryBuilders.nestedQuery(ArticleField.CRITERIA_CID.code(),
          queryCid, ScoreMode.None));
    }
    aggregate.getSubIds().stream()
        .map(item -> addFilterForCriteriaCVP(queryCid, item))
        .forEach(bool::should);
    return bool;
  }

  private NestedQueryBuilder addFilterForCriteriaCVP(final TermsQueryBuilder termsQueryBuilder,
      ArticleAggregateMultiLevel item) {
    final BoolQueryBuilder boolCVP = QueryBuilders.boolQuery();
    boolCVP.must(QueryBuilders.termsQuery(ArticleField.CRITERIA_CVP_RAW.value(), item.getId()));
    boolCVP.must(termsQueryBuilder);
    return QueryBuilders.nestedQuery(ArticleField.CRITERIA_CID.code(), boolCVP, ScoreMode.None);
  }

}
