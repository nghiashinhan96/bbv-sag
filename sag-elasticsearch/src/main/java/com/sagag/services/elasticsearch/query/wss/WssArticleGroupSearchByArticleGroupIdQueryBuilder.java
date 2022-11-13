package com.sagag.services.elasticsearch.query.wss;

import com.sagag.services.elasticsearch.criteria.wss.WssArticleGroupSearchByArticleGroupIdCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.WssArticleGroupQueryBuilders;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Slf4j
public class WssArticleGroupSearchByArticleGroupIdQueryBuilder
    implements ISearchQueryBuilder<WssArticleGroupSearchByArticleGroupIdCriteria>, IAggregationBuilder {

  @Override
  public SearchQuery buildQuery(WssArticleGroupSearchByArticleGroupIdCriteria criteria, Pageable pageable,
      String... indices) {

    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.notEmpty(indices, "The given indices must not be empty");

    final BoolQueryBuilder query = QueryBuilders.boolQuery();
    query.must(QueryBuilders.matchQuery(Index.WssArticleGroup.ARTICLE_GROUP_ARTGRP.fullQField(),
        criteria.getArticleGroupId()));
    WssArticleGroupQueryBuilders.buildLockArticleGroupByFieldQuery(query,
        Index.WssArticleGroup.ARTICLE_GROUP_PARENTID, StringUtils.EMPTY);
    log.debug("Query : {}", query);
    return new NativeSearchQueryBuilder().withIndices(indices).withQuery(query).build();

  }

}
