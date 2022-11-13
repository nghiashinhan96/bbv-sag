package com.sagag.services.elasticsearch.query.wss.builder;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.elasticsearch.criteria.wss.WssArticlegroupSearchTermCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.enums.Index.WssArticleGroup;
import com.sagag.services.elasticsearch.enums.IndexFieldType;
import com.sagag.services.elasticsearch.query.articles.WssArticleGroupQueryBuilders;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class WssArticleGroupSearchTermQueryBuilder
    extends AbstractWssArticleGroupSearchTermQueryBuilder {

  @Override
  public QueryBuilder build(WssArticlegroupSearchTermCriteria criteria) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    final String articleGroup = criteria.getArticleGroup();
    final String articleGroupDesc = criteria.getArticleGroupDesc();

    if (StringUtils.isNotBlank(articleGroup)) {
      buildSearchArticleGroupByFieldQuery(queryBuilder, Index.WssArticleGroup.ARTICLE_GROUP_ARTGRP,
          articleGroup);
    }
    if (StringUtils.isNotBlank(articleGroupDesc)) {
      buildSearchArticleGroupByFieldQuery(queryBuilder,
          Index.WssArticleGroup.ARTICLE_GROUP_ARTGRP_DESC, articleGroupDesc);
    }

    WssArticleGroupQueryBuilders.buildLockArticleGroupByFieldQuery(queryBuilder,
        Index.WssArticleGroup.ARTICLE_GROUP_PARENTID, StringUtils.EMPTY);
    return queryBuilder;
  }

  private void buildSearchArticleGroupByFieldQuery(final BoolQueryBuilder queryBuilder,
      final WssArticleGroup field, final String keyWord) {
    final boolean isPerfectMatched = !keyWord.contains(SagConstants.WILDCARD);
    if (isPerfectMatched) {
      queryBuilder.must(QueryBuilders.matchQuery(field.fullQField(), keyWord));

    } else {
      String[] fields = { field.fullQField() };
      queryBuilder.must(buildFilteredQuery(keyWord, fields, Collections.emptyMap(),
          IndexFieldType.NONE_NESTED, field));
    }
  }

  private static QueryBuilder buildFilteredQuery(final String freetext, final String[] fields,
      final Map<String, Float> attrBoost, final IndexFieldType fieldType,
      final WssArticleGroup artGroupField) {
    return WssArticleGroupQueryBuilders.filterQuery(attrBoost, fieldType, artGroupField, true)
        .apply(freetext, fields);
  }
}
