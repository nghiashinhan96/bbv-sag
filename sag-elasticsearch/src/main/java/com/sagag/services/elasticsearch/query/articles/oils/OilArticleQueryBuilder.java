package com.sagag.services.elasticsearch.query.articles.oils;

import com.sagag.services.elasticsearch.common.OilConstants;
import com.sagag.services.elasticsearch.criteria.article.OilArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class OilArticleQueryBuilder
  extends AbstractArticleQueryBuilder<OilArticleSearchCriteria> implements IAggregationBuilder {

  private static final String[] OIL_GAIDS =
      new String[] { "9780", "9781", "9782", "9783", "9784", "9785", "9786", "9787", "9788", "9789",
          "9791", "9792", "9565", "9563", "8563", "8562", "8561", "3354", "5964", "5962", "5953",
          "5873", "3226", "4733", "3422", "3421", "3353", "2410", "2409", "2401", "1862", "1667",
          "1620", "3224", "4779", "12000", "19242", "21262", "25957", "25954", "25953", "21261",
          "7638", "15947", "5685", "3225", "3069", "1601", "3462", "4765", "1815",
          "1602", "3356", "71", "3357", "12006", "20597" };

  @Override
  public SearchQuery buildQuery(OilArticleSearchCriteria criteria, Pageable pageable,
    String... indices) {
    final BoolQueryBuilder queryBuilder =
        applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
        .apply(commonQueryBuilder(criteria));

    aggFilterBuilders.forEach(builder -> builder.addFilter(queryBuilder, criteria));

    log.debug("Query = {}", queryBuilder);

    final NativeSearchQueryBuilder searchQueryBuilder = ArticleQueryUtils.nativeQueryBuilder(
        defaultPageable(pageable), indices)
        .withQuery(queryBuilder);

    aggregated(searchQueryBuilder, ArticleField.SUPPLIER_RAW);

    return searchQueryBuilder.build();
  }

  private static BoolQueryBuilder commonQueryBuilder(final OilArticleSearchCriteria criteria) {
    final BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();

    // Filter by oil gen_art_ids
    searchQuery.must(QueryBuilders.termsQuery(ArticleField.GA_ID.value(), OIL_GAIDS));

    // Filter by oil brand
    if (CollectionUtils.isNotEmpty(criteria.getBrands())) {
      searchQuery
      .must(QueryBuilders.termsQuery(ArticleField.SUPPLIER_RAW.value(), criteria.getBrands()));
    }

    // Filter by vehicle
    Optional.ofNullable(criteria.getVehicles())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpRawQuery(OilConstants.VEHICLE_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by aggregate
    Optional.ofNullable(criteria.getAggregates())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpRawQuery(OilConstants.AGGREGATE_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by viscosity
    Optional.ofNullable(criteria.getViscosities())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpRawQuery(OilConstants.VISCOSITY_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by bottle size
    Optional.ofNullable(criteria.getBottleSizes())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpRawQuery(OilConstants.BOTTLE_SIZE_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by approved
    Optional.ofNullable(criteria.getApproved())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpRawQuery(OilConstants.APPROVED_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by specification
    Optional.ofNullable(criteria.getSpecifications())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpRawQuery(OilConstants.SPECIFICATION_CID, value))
    .ifPresent(searchQuery::must);

    return searchQuery;
  }
}
