package com.sagag.services.elasticsearch.query.wss;

import com.sagag.services.elasticsearch.criteria.wss.WssArticleGroupSearchCriteria;
import com.sagag.services.elasticsearch.criteria.wss.WssArticleGroupSearchSortCriteria;
import com.sagag.services.elasticsearch.criteria.wss.WssArticlegroupSearchTermCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import com.sagag.services.elasticsearch.query.wss.builder.AbstractWssArticleGroupSearchTermQueryBuilder;

import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class WssArticleGroupSearchQueryBuilder
    implements ISearchQueryBuilder<WssArticleGroupSearchCriteria>, IAggregationBuilder {

  @Autowired
  private AbstractWssArticleGroupSearchTermQueryBuilder searchTermQueryBuilders;

  @Override
  public SearchQuery buildQuery(WssArticleGroupSearchCriteria criteria, Pageable pageable,
      String... indices) {
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.notEmpty(indices, "The given indices must not be empty");

    final BoolQueryBuilder query = QueryBuilders.boolQuery();
    query.must(commonQueryBuilder(criteria.getSearchTerm()));
    final WssArticleGroupSearchSortCriteria sort = criteria.getSort();

    NativeSearchQueryBuilder nativeQuery =
        new NativeSearchQueryBuilder().withIndices(indices).withPageable(pageable).withQuery(query);
    if (sort != null) {
      getSortingQueries(sort).forEach(nativeQuery::withSort);
    }
    log.debug("Query : {}", nativeQuery);
    return nativeQuery.build();
  }

  private QueryBuilder commonQueryBuilder(final WssArticlegroupSearchTermCriteria searchTerm) {
    return searchTermQueryBuilders.build(searchTerm);
  }

  private static List<FieldSortBuilder> getSortingQueries(
      final WssArticleGroupSearchSortCriteria sort) {
    final List<FieldSortBuilder> sortedBuilders = new ArrayList<>();

    Optional.ofNullable(sort.getArticleGroup())
        .map(sortOrder -> SortBuilders
            .fieldSort(Index.WssArticleGroup.ARTICLE_GROUP_ARTGRP.fullQField()).order(sortOrder))
        .ifPresent(sortedBuilders::add);

    return sortedBuilders;
  }

}
