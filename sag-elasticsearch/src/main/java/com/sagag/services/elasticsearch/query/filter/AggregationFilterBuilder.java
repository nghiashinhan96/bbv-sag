package com.sagag.services.elasticsearch.query.filter;

import com.sagag.services.elasticsearch.criteria.article.ArticleAggregateCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.util.List;

@FunctionalInterface
public interface AggregationFilterBuilder {

  /**
   * Adds the aggregation filter to search query.
   *
   * @param searchQueryBuilder
   * @param aggCriteria
   */
  void addFilter(BoolQueryBuilder searchQueryBuilder, ArticleAggregateCriteria aggCriteria);

  default void addFilter(BoolQueryBuilder searchQueryBuilder, ArticleField artField,
      List<String> filters) {
    if (searchQueryBuilder == null || artField == null || CollectionUtils.isEmpty(filters)) {
      return;
    }
    final TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(artField.value(), filters);
    if (artField.isNested()) {
      searchQueryBuilder.must(
          QueryBuilders.nestedQuery(artField.code(), termsQueryBuilder, ScoreMode.None));
    } else {
      searchQueryBuilder.must(termsQueryBuilder);
    }
  }

}
