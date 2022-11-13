package com.sagag.services.elasticsearch.query.articles.article.freetext;

import com.sagag.services.elasticsearch.config.ArticleIdFieldMapper;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.IndexFieldType;
import com.sagag.services.elasticsearch.parser.ArticleFreetextStringParser;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryBuilders;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;
import com.sagag.services.elasticsearch.query.articles.article.AggregationArticleListQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.AggregationFreeTextQueryBuilder;
import com.sagag.services.elasticsearch.utils.ElasticsearchConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FreetextArticleQueryBuilder
    extends AbstractArticleQueryBuilder<KeywordArticleSearchCriteria>
    implements IAggregationBuilder {

  @Autowired
  private AggregationArticleListQueryBuilder articleListQuery;

  @Autowired
  private AggregationFreeTextQueryBuilder freeTextQuery;

  @Autowired
  private ArticleFreetextStringParser freetextStringParser;

  @Autowired
  private IFreetextFields freetextFields;

  @Autowired
  private ArticleIdFieldMapper articleIdFieldMapper;

  /**
   * Builds the freetext query search using {@link QueryStringQueryBuilder}. <br/>
   * Keeps this in here and refactor later because this method will be used for filtering service.
   *
   * @param criteria the free text criteria to search
   * @return the search query builder
   */
  @Override
  public SearchQuery buildQuery(KeywordArticleSearchCriteria criteria, Pageable pageable,
      String... indices) {

    final BoolQueryBuilder searchQuery = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
        .apply(freetextBoolQueryBuilder(criteria));

    aggFilterBuilders.forEach(builder -> builder.addFilter(searchQuery, criteria));

    log.debug("Query = {}", searchQuery);

    final NativeSearchQueryBuilder queryBuilder = ArticleQueryUtils.nativeQueryBuilder(
        pageable, indices).withQuery(searchQuery);

    // Add sort builder
    Stream.of(SortBuilders.scoreSort(),
        SortBuilders.fieldSort(ArticleField.ART_NUMBER.value()).order(SortOrder.ASC))
    .forEach(queryBuilder::withSort);

    if (!criteria.isAggregated()) {
      return queryBuilder.build();
    }

    if (!criteria.isUseMultipleAggregation()) {
      articleListQuery.buildAggregation(queryBuilder);
      return queryBuilder.build();
    }
    if (!criteria.isNeedSubAggregated()) {
      aggregated(queryBuilder, ArticleField.GA_ID);
      aggregated(queryBuilder, ArticleField.SUPPLIER_RAW);
      return queryBuilder.build();
    }
    freeTextQuery.buildAggregation(queryBuilder);
    return queryBuilder.build();
  }

  protected BoolQueryBuilder freetextBoolQueryBuilder(final KeywordArticleSearchCriteria criteria) {
    // normalize the query for ES, #1565
    final String normalizedText = freetextStringParser.apply(criteria.getText(),
        new Object[] {criteria.isPerfectMatched()});
    log.debug("Searching articles by free text = {}", normalizedText);
    if (criteria.isPerfectMatched()) {
      return exactMatchedFreetextBoolQueryBuilder(criteria.isUsePartsExt(),
          criteria.isDirectMatch(), normalizedText);
    }

    List<QueryBuilder> queries = new ArrayList<>();
    queries.add(createFilterQueryForArticle(normalizedText));
    queries.add(createFilterQueryForCriteria(normalizedText));
    queries.add(createFilterPartQuery(criteria.isUsePartsExt(), normalizedText));
    queries.add(createFilterQueryForProductTextInfo(normalizedText));

    if (!CollectionUtils.isEmpty(criteria.getPreferSagsysIds())) {
      criteria.getPreferSagsysIds().stream()
          .filter(StringUtils::isNotBlank)
          .forEach(idSagsys -> queries.add(preferArticleId(idSagsys)));
    }

    final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    queries.stream().forEach(boolQuery::should);
    return QueryBuilders.boolQuery().must(boolQuery);
  }

  private QueryBuilder createFilterQueryForProductTextInfo(final String freetext) {
    return buildFilteredQuery(freetext, freetextFields.productInfoTextFields(),
      freetextFields.attributesBoost(), IndexFieldType.NONE_NESTED, ArticleField.REFS);
  }

  private QueryBuilder createFilterQueryForArticle(final String freetext) {
    return buildFilteredQuery(freetext, freetextFields.refsFields(),
      freetextFields.attributesBoost(), IndexFieldType.NONE_NESTED, ArticleField.REFS);
  }

  private QueryBuilder createFilterQueryForCriteria(final String freetext) {
    return buildFilteredQuery(freetext, freetextFields.criteriaFields(),
      freetextFields.attributesBoost(), IndexFieldType.NESTED, ArticleField.CRITERIA);
  }

  private QueryBuilder createFilterQueryForParts(final String freetext) {
    return buildFilteredQuery(freetext, freetextFields.partFields(),
      freetextFields.attributesBoost(), IndexFieldType.NESTED, ArticleField.PARTS);
  }

  private QueryBuilder createFilterQueryForPartsExts(final String freetext) {
    return buildFilteredQuery(freetext, freetextFields.partsExtFields(),
      freetextFields.attributesBoost(), IndexFieldType.NESTED, ArticleField.PARTS_EXT);
  }

  private static QueryBuilder buildFilteredQuery(final String freetext, final String[] fields,
    final Map<String, Float> attrBoost, final IndexFieldType fieldType, final ArticleField artField) {
    return ArticleQueryBuilders.filterQuery(attrBoost, fieldType, artField, true).apply(freetext, fields);
  }

  private BoolQueryBuilder exactMatchedFreetextBoolQueryBuilder(boolean isUsePartsExt,
      boolean isDirectMatch, final String normalizedText) {
    List<QueryBuilder> queries = new ArrayList<>();
    queries.add(createFilterQueryForExactMatch(normalizedText));
    // Check non direct-match to add part query
    if (!isDirectMatch) {
      queries.add(createFilterPartQuery(isUsePartsExt, normalizedText));
    }
    final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    queries.forEach(boolQuery::should);
    return QueryBuilders.boolQuery().must(boolQuery);
  }

  private QueryBuilder createFilterQueryForExactMatch(final String freetext) {
    return ArticleQueryBuilders
      .filterQuery(freetextFields.attributesBoost(), IndexFieldType.NONE_NESTED,
        ArticleField.REFS, true)
      .apply(freetext, freetextFields.refFieldsFullText());
  }

  private QueryBuilder preferArticleId(final String sagsysId) {
    return QueryBuilders.constantScoreQuery(
      QueryBuilders.termQuery(articleIdFieldMapper.getField().value(), sagsysId))
      .boost(ElasticsearchConstants.MAX_BOOST);
  }

  private QueryBuilder createFilterPartQuery(boolean isUsePartsExt, String normalizedText) {
    return isUsePartsExt ? createFilterQueryForPartsExts(normalizedText)
        : createFilterQueryForParts(normalizedText);
  }
}
