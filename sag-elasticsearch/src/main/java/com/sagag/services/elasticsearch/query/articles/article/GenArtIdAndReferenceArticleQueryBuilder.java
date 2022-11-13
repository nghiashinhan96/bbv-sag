package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.criteria.article.ReferenceAndArtNumSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GenArtIdAndReferenceArticleQueryBuilder
    extends AbstractArticleQueryBuilder<ReferenceAndArtNumSearchCriteria>
    implements IAggregationBuilder {

  @Autowired
  private AggregationArticleListQueryBuilder articleListQuery;

  @Autowired
  private AggregationFreeTextQueryBuilder freeTextQuery;

  @Autowired
  private AggregationOEAndArtNumQueryBuilder aggregationOEAndArtNumQueryBuilder;

  @Override
  public SearchQuery buildQuery(ReferenceAndArtNumSearchCriteria criteria, Pageable pageable,
    String... indices) {
    final BoolQueryBuilder searchQuery = buildSearchQuery(criteria);
    if (!searchQuery.hasClauses()) {
      return null;
    }
    final BoolQueryBuilder query = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
      .apply(searchQuery);
    aggFilterBuilders.forEach(builder -> builder.addFilter(query, criteria));
    log.debug("Query = {}", query);

    final NativeSearchQueryBuilder queryBuilder = ArticleQueryUtils
      .nativeQueryBuilder(pageable, indices).withQuery(query);

    if (!criteria.isAggregated()) {
      return queryBuilder.build();
    }

    if (!criteria.isFromFreetextSearch()) {
      aggregationOEAndArtNumQueryBuilder.buildAggregation(queryBuilder);
      return queryBuilder.build();
    }

    if (!criteria.isUseMultipleAggregation()) {
      articleListQuery.buildAggregation(queryBuilder);
      return queryBuilder.build();
    }
    if (!criteria.isNeedSubAggregated()) {
      aggregated(queryBuilder, ArticleField.GA_ID);
      return queryBuilder.build();
    }
    freeTextQuery.buildAggregation(queryBuilder);
    return queryBuilder.build();
  }

  private static BoolQueryBuilder buildSearchQuery(ReferenceAndArtNumSearchCriteria criteria) {
    final BoolQueryBuilder query = QueryBuilders.boolQuery();

    final BoolQueryBuilder artNrQuery = QueryBuilders.boolQuery();
    // Build query for article number
    Optional.ofNullable(criteria.getArticleNrs())
      .filter(CollectionUtils::isNotEmpty)
      .map(artNrs -> QueryBuilders.termsQuery(ArticleField.ART_NUMBER.field(), artNrs))
      .ifPresent(artNrQuery::should);
    if (MapUtils.isEmpty(criteria.getReferenceByGenArtIdMap())) {
      return query;
    }

    final BoolQueryBuilder referenceQueryBuilder = QueryBuilders.boolQuery();

    // Build query for gaId
    final boolean isUseGenArtIdInQuery = criteria.isUseGenArtIdInQuery();
    Optional.ofNullable(criteria.getReferenceByGenArtIdMap())
      .filter(map -> BooleanUtils.isTrue(isUseGenArtIdInQuery))
      .map(Map::keySet)
      .map(gaIds -> QueryBuilders.termsQuery(ArticleField.GA_ID.field(), gaIds))
      .ifPresent(referenceQueryBuilder::must);

    Optional.ofNullable(criteria.getReferenceByGenArtIdMap())
      .map(referenceCodeConverter())
      .map(GenArtIdAndReferenceArticleQueryBuilder::buildPartCodeAndPartExtCodeQuery)
      .ifPresent(referenceQueryBuilder::must);

    if (referenceQueryBuilder.hasClauses()) {
      artNrQuery.should(referenceQueryBuilder);
    }

    if (!artNrQuery.hasClauses()) {
      return query;
    }
    return query.must(artNrQuery);
  }

  private static NestedQueryBuilder buildPartCodeQuery(ArticleField field, List<String> references) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
      .should(QueryBuilders.termsQuery(field.field(), references));
    return QueryBuilders.nestedQuery(field.code(),
      queryBuilder, ScoreMode.None);
  }

  private static BoolQueryBuilder buildPartCodeAndPartExtCodeQuery(List<String> references) {
    return QueryBuilders.boolQuery()
      .should(buildPartCodeQuery(ArticleField.PARTS_NUMBER, references))
      .should(buildPartCodeQuery(ArticleField.PARTS_EXT_NUMBER, references));
  }

  private static Function<Map<String, Set<String>>, List<String>> referenceCodeConverter() {
    return referenceMap -> referenceMap.values().stream().flatMap(Collection::stream)
      .collect(Collectors.toList());
  }

}
