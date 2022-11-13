package com.sagag.services.elasticsearch.query.articles.tyres;

import static com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils.buildCvpListQuery;
import static com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils.buildCvpQuery;

import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.elasticsearch.criteria.article.BaseTyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

@Component
public class TyreSearchQueryConverter
    implements BiFunction<BaseTyreArticleSearchCriteria, Set<String>, BoolQueryBuilder> {

  private static final String[] PRIORITY_PRODUCT_GROUPS = new String[] {
      TyreConstants.PRIORITY_PRODUCT_GROUP_R,
      TyreConstants.PRIORITY_PRODUCT_GROUP_I
  };

  @Override
  public BoolQueryBuilder apply(final BaseTyreArticleSearchCriteria criteria,
    final Set<String> genArtIds) {
    final BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();

    // Filter by category
    Optional.ofNullable(genArtIds)
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> QueryBuilders.termsQuery(ArticleField.GA_ID.value(), value))
    .ifPresent(searchQuery::must);

    // Filter by supplier
    Optional.ofNullable(criteria.getSupplier())
    .filter(StringUtils::isNotBlank)
    .map(value -> QueryBuilders.matchQuery(ArticleField.SUPPLIER.value(),
        StringUtils.lowerCase(value)))
    .ifPresent(searchQuery::must);

    // Filter by width
    Optional.ofNullable(criteria.getWidthCvp())
    .filter(StringUtils::isNotBlank)
    .map(value -> buildCvpQuery().apply(TyreConstants.WIDTH_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by height
    Optional.ofNullable(criteria.getHeightCvp())
    .filter(StringUtils::isNotBlank)
    .map(value -> buildCvpQuery().apply(TyreConstants.HEIGHT_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by radius
    Optional.ofNullable(criteria.getRadiusCvp())
    .filter(StringUtils::isNotBlank)
    .map(value -> buildCvpQuery().apply(TyreConstants.RADIUS_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by speed index
    final BoolQueryBuilder speedIndexQuery = QueryBuilders.boolQuery();
    CollectionUtils.emptyIfNull(criteria.getSpeedIndexCvps())
        .stream()
        .filter(StringUtils::isNotBlank)
        .map(value -> buildCvpQuery().apply(TyreConstants.SPEED_INDEX_CID,
            StringUtils.lowerCase(value)))
        .forEach(speedIndexQuery::should);
    searchQuery.must(speedIndexQuery);

    // Filter by tyre segment
    final BoolQueryBuilder tyreSegmentQuery = QueryBuilders.boolQuery();
    CollectionUtils.emptyIfNull(criteria.getTyreSegmentCvps())
        .stream()
        .filter(StringUtils::isNotBlank)
        .map(value -> buildCvpListQuery().apply(TyreConstants.TYRE_SEGMENT_CID,
            ArticleQueryUtils.analyzeCvp(value)))
        .forEach(tyreSegmentQuery::should);
    searchQuery.must(tyreSegmentQuery);

    // Filter by runflat
    Optional.ofNullable(criteria.isRunflat())
    .filter(BooleanUtils::isTrue)
    .map(value -> buildCvpListQuery().apply(TyreConstants.TYRE_SEGMENT_CID,
        ArticleQueryUtils.analyzeCvp(TyreConstants.RUNFLAT_CVP)))
    .ifPresent(searchQuery::must);

    // Filter by spike
    Optional.ofNullable(criteria.isSpike())
    .filter(BooleanUtils::isTrue)
    .map(value -> buildCvpListQuery().apply(TyreConstants.TYRE_SEGMENT_CID,
        ArticleQueryUtils.analyzeCvp(TyreConstants.SPIKE_CVP)))
    .ifPresent(searchQuery::must);

    // Filter by load_index, reference ticket #1566
    final BoolQueryBuilder loadIndexQuery = QueryBuilders.boolQuery();
    CollectionUtils.emptyIfNull(criteria.getLoadIndexCvps())
        .stream()
        .filter(StringUtils::isNotBlank)
        .map(value -> buildCvpQuery().apply(TyreConstants.LOAD_INDEX_CID, value))
        .forEach(loadIndexQuery::should);
    searchQuery.must(loadIndexQuery);

    searchQuery.should(QueryBuilders.termsQuery(ArticleField.SAG_PRODUCT_GROUP.value(),
        PRIORITY_PRODUCT_GROUPS));

    return searchQuery;
  }

}
