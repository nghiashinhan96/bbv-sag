package com.sagag.services.elasticsearch.query.articles.batteries;

import static com.sagag.services.elasticsearch.common.BatteryConstants.AMPERE_HOUR_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.INTERCONNECTION_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.LENGTH_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.POLE_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.VOLTAGE_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.WIDTH_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.WITHOUT_START_STOP_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.WITH_START_STOP_CID;

import com.sagag.services.elasticsearch.common.BatteryConstants;
import com.sagag.services.elasticsearch.criteria.article.BatteryArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BatteryArticleQueryBuilder
    extends AbstractArticleQueryBuilder<BatteryArticleSearchCriteria> implements IAggregationBuilder {

  private static final String[] BATTERY_GEN_ART_IDS = new String[] { "1", "3465", "300001" };

  @Override
  public SearchQuery buildQuery(BatteryArticleSearchCriteria criteria, Pageable pageable,
    String... indices) {
    final BoolQueryBuilder queryBuilder = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
      .apply(commonQueryBuilder(criteria));

    aggFilterBuilders.forEach(builder -> builder.addFilter(queryBuilder, criteria));

    final NativeSearchQueryBuilder searchQueryBuilder = ArticleQueryUtils.nativeQueryBuilder(
        defaultPageable(pageable), indices)
        .withQuery(queryBuilder);

    aggregated(searchQueryBuilder, ArticleField.SUPPLIER_RAW);

    return searchQueryBuilder.build();
  }

  private static BoolQueryBuilder commonQueryBuilder(final BatteryArticleSearchCriteria criteria) {
    final BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();

    // Filter by battery gen_art_ids
    searchQuery.must(QueryBuilders.termsQuery(ArticleField.GA_ID.value(), BATTERY_GEN_ART_IDS));

    // Filter by voltages
    Optional.ofNullable(criteria.getVoltages())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpQuery(VOLTAGE_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by ampere hours
    Optional.ofNullable(criteria.getAmpereHours())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpQuery(AMPERE_HOUR_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by lengths
    Optional.ofNullable(criteria.getLengths())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpQuery(LENGTH_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by widths
    Optional.ofNullable(criteria.getWidths())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpQuery(WIDTH_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by heights
    Optional.ofNullable(criteria.getHeights())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpQuery(BatteryConstants.HEIGHT_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by interconnections
    Optional.ofNullable(criteria.getInterconnections())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpQuery(INTERCONNECTION_CID, value))
    .ifPresent(searchQuery::must);

    // Filter by type of pole

    Optional.ofNullable(criteria.getTypeOfPoles())
    .filter(CollectionUtils::isNotEmpty)
    .map(value -> buildCvpQuery(POLE_CID, analyzeCvps(value)))
    .ifPresent(searchQuery::must);


    // Filter by without start stop
    Optional.ofNullable(criteria.isWithoutStartStop())
    .filter(BooleanUtils::isTrue)
    .map(value -> QueryBuilders.boolQuery().must(
        QueryBuilders.termQuery(ArticleField.CRITERIA_CID.value(), WITHOUT_START_STOP_CID)))
    .map(queryBuilder -> QueryBuilders.nestedQuery(ArticleField.CRITERIA.value(), queryBuilder,
        ScoreMode.None))
    .ifPresent(searchQuery::must);


    // Filter by with start stop
    Optional.ofNullable(criteria.isWithStartStop())
    .filter(BooleanUtils::isTrue)
    .map(value -> QueryBuilders.boolQuery().must(
        QueryBuilders.termQuery(ArticleField.CRITERIA_CID.value(), WITH_START_STOP_CID)))
    .map(queryBuilder -> QueryBuilders.nestedQuery(ArticleField.CRITERIA.value(), queryBuilder,
        ScoreMode.None))
    .ifPresent(searchQuery::must);

    return searchQuery;
  }

}
