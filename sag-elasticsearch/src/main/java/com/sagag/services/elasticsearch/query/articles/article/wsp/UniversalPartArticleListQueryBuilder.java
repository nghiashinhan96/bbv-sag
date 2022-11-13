package com.sagag.services.elasticsearch.query.articles.article.wsp;

import com.google.common.collect.Lists;
import com.sagag.services.common.domain.CriteriaDto;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchTerm;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.AggregationPathMultiLevel;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;
import com.sagag.services.elasticsearch.query.unitrees.UnitreeQueryUtils;
import com.sagag.services.elasticsearch.utils.ElasticsearchConstants;
import com.sagag.services.elasticsearch.utils.UniversalPartQueryUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UniversalPartArticleListQueryBuilder extends
    AbstractArticleQueryBuilder<UniversalPartArticleSearchCriteria> implements IAggregationBuilder {

  @Override
  public SearchQuery buildQuery(UniversalPartArticleSearchCriteria criteria, Pageable pageable,
      String... indices) {
    final String[] affNameLocks = criteria.getAffNameLocks();
    final boolean isSaleOnBehalf = criteria.isSaleOnBehalf();

    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

    Optional.ofNullable(criteria.getIncludeTerm())
        .ifPresent(inclTerm -> buildIncludeCriteriaQuery(inclTerm, queryBuilder));


    Optional.ofNullable(criteria.getExcludeTerm())
        .ifPresent(exclTerm -> buildExcludeCriteriaQuery(exclTerm, queryBuilder));

    final BoolQueryBuilder query =
        applyCommonQueryBuilder(affNameLocks, isSaleOnBehalf).apply(queryBuilder);

    aggFilterBuilders.forEach(builder -> builder.addFilter(queryBuilder, criteria));

    AggregationPathMultiLevel supplierId =
        new AggregationPathMultiLevel(ArticleField.ID_PRODUCT_BRAND);
    AggregationPathMultiLevel gaId = new AggregationPathMultiLevel(ArticleField.GA_ID);
    AggregationPathMultiLevel supplierRaw =
        new AggregationPathMultiLevel(ArticleField.SUPPLIER_RAW);
    AggregationPathMultiLevel criteriaSubCvpRaw =
        new AggregationPathMultiLevel(ArticleField.CRITERIA_CID, ArticleField.CRITERIA_CVP_RAW);

    NativeSearchQueryBuilder searchQueryBuilder =
        ArticleQueryUtils.nativeQueryBuilder(pageable, indices).withQuery(query);
    aggregated(searchQueryBuilder, supplierId, gaId, supplierRaw, criteriaSubCvpRaw);

    searchQueryBuilder
        .withSort(SortBuilders.fieldSort(ArticleField.SUPPLIER_RAW.value()).order(SortOrder.ASC));
    return searchQueryBuilder.build();
  }

  private void buildExcludeCriteriaQuery(UniversalPartArticleSearchTerm excludeSearchTerm,
      final BoolQueryBuilder queryBuilder) {
    if (CollectionUtils.isNotEmpty(excludeSearchTerm.getGenArts())) {
      queryBuilder.mustNot(
          QueryBuilders.termsQuery(ArticleField.GA_ID.value(), excludeSearchTerm.getGenArts()));
    }
    if (CollectionUtils.isNotEmpty(excludeSearchTerm.getArticleIds())) {
      queryBuilder.mustNot(buildIdSagsysQuery(excludeSearchTerm.getArticleIds()));
    }
    if (CollectionUtils.isNotEmpty(excludeSearchTerm.getArticleGroups())) {
      queryBuilder.mustNot(buildArticleGroupQuery(excludeSearchTerm.getArticleGroups()));
    }
    if (CollectionUtils.isNotEmpty(excludeSearchTerm.getBrandIds())) {
      queryBuilder.mustNot(QueryBuilders.termsQuery(ArticleField.ID_PRODUCT_BRAND.value(),
          excludeSearchTerm.getBrandIds()));
    }
    if (CollectionUtils.isNotEmpty(excludeSearchTerm.getCriterion())) {
      BoolQueryBuilder cvpQuery = buildArticleCriteriaQuery(excludeSearchTerm.getCriterion());
      queryBuilder.mustNot(cvpQuery);
    }
  }

  private void buildIncludeCriteriaQuery(UniversalPartArticleSearchTerm includeSearchTerm,
      final BoolQueryBuilder queryBuilder) {
    if (CollectionUtils.isNotEmpty(includeSearchTerm.getGenArts())) {
      queryBuilder.must(
          QueryBuilders.termsQuery(ArticleField.GA_ID.value(), includeSearchTerm.getGenArts()));
    }
    if (CollectionUtils.isNotEmpty(includeSearchTerm.getArticleIds())) {
      queryBuilder.must(buildIdSagsysQuery(includeSearchTerm.getArticleIds()));
    }
    if (CollectionUtils.isNotEmpty(includeSearchTerm.getArticleGroups())) {
      queryBuilder.must(buildArticleGroupQuery(includeSearchTerm.getArticleGroups()));
    }
    if (CollectionUtils.isNotEmpty(includeSearchTerm.getBrandIds())) {
      queryBuilder.must(QueryBuilders.termsQuery(ArticleField.ID_PRODUCT_BRAND.value(),
          includeSearchTerm.getBrandIds()));
    }
    if (CollectionUtils.isNotEmpty(includeSearchTerm.getCriterion())) {
      BoolQueryBuilder cvpQuery = buildArticleCriteriaQuery(includeSearchTerm.getCriterion());
      queryBuilder.must(cvpQuery);
    }
  }

  private static BoolQueryBuilder buildArticleGroupQuery(List<String> articleGroups) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    Map<ArticleField, List<String>> productGroupMap =
        UniversalPartQueryUtils.extractArticleGroupByLevel(articleGroups);

    if (CollectionUtils.isNotEmpty(productGroupMap.get(ArticleField.PRODUCT_GROUP))) {
      queryBuilder.should(QueryBuilders.termsQuery(ArticleField.PRODUCT_GROUP.value(),
          productGroupMap.get(ArticleField.PRODUCT_GROUP)));
    }

    if (CollectionUtils.isNotEmpty(productGroupMap.get(ArticleField.PRODUCT_GROUP_2))) {
      queryBuilder.should(QueryBuilders.termsQuery(ArticleField.PRODUCT_GROUP_2.value(),
          productGroupMap.get(ArticleField.PRODUCT_GROUP_2)));
    }

    if (CollectionUtils.isNotEmpty(productGroupMap.get(ArticleField.PRODUCT_GROUP_3))) {
      queryBuilder.should(QueryBuilders.termsQuery(ArticleField.PRODUCT_GROUP_3.value(),
          productGroupMap.get(ArticleField.PRODUCT_GROUP_2)));
    }

    if (CollectionUtils.isNotEmpty(productGroupMap.get(ArticleField.PRODUCT_GROUP_4))) {
      queryBuilder.should(QueryBuilders.termsQuery(ArticleField.PRODUCT_GROUP_4.value(),
          productGroupMap.get(ArticleField.PRODUCT_GROUP_4)));
    }

    return queryBuilder;
  }

  private static BoolQueryBuilder buildIdSagsysQuery(final List<String> sagsysIds) {
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    sagsysIds.forEach(sagsysId -> queryBuilder.should(preferIdSagsys(sagsysId)));
    return queryBuilder;
  }

  private static QueryBuilder preferIdSagsys(final String sagsysId) {
    return QueryBuilders
        .constantScoreQuery(QueryBuilders.termQuery(ArticleField.ID_SAGSYS.value(), sagsysId))
        .boost(ElasticsearchConstants.MAX_BOOST);
  }

  private static BoolQueryBuilder buildArticleCriteriaQuery(List<CriteriaDto> criterion) {
    BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
    final Map<String, List<String>> criteriaMap =
        ListUtils.emptyIfNull(criterion).stream().collect(Collectors.groupingBy(CriteriaDto::getCid,
            Collectors.mapping(CriteriaDto::getCvp, Collectors.toList())));
    List<NestedQueryBuilder> nestedCriteriaQueries = Lists.newArrayList();
    criteriaMap.forEach((k, v) -> nestedCriteriaQueries
        .add(UnitreeQueryUtils.buildCvpRawListQuery().apply(NumberUtils.createInteger(k), v)));
    nestedCriteriaQueries.forEach(queryBuilder::must);
    return queryBuilder;
  }

}
