package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.elasticsearch.criteria.vehicle.VehicleFilteringCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchSortCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.IAggregationQueryBuilder;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import com.sagag.services.elasticsearch.query.vehicles.VehicleQueryUtils;
import com.sagag.services.elasticsearch.query.vehicles.VehicleSearchAggregationMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Slf4j
public class VehicleAdvanceSearchQueryBuilder
    implements ISearchQueryBuilder<VehicleSearchCriteria>, IAggregationBuilder {

  private static final Index.Vehicle[] AGGREGATION_VEHICLE_FILTERING_FIELDS = {
      Index.Vehicle.VEHICLE_ADVANCE_NAME_RAW,
      Index.Vehicle.VEHICLE_BUILT_YEAR_FROM,
      Index.Vehicle.VEHICLE_BODY_TYPE_RAW,
      Index.Vehicle.VEHICLE_FUEL_TYPE_RAW,
      Index.Vehicle.VEHICLE_ZYLINDER,
      Index.Vehicle.VEHICLE_ENGINE,
      Index.Vehicle.VEHICLE_CAPACITY_CC_TECH,
      Index.Vehicle.VEHICLE_ENGINE_CODE_RAW,
      Index.Vehicle.VEHICLE_DRIVE_TYPE_RAW
    };

  @Override
  public SearchQuery buildQuery(VehicleSearchCriteria criteria, Pageable pageable, String... indices) {
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.notNull(criteria.getSearchTerm(), "The given search term must not be null");
    Assert.notEmpty(indices, "The given indices must not be empty");

    Integer idMake = criteria.getSearchTerm().getMakeId();
    Integer idModel = criteria.getSearchTerm().getModelId();
    if (Objects.isNull(idMake) || Objects.isNull(idModel)) {
      return new NativeSearchQueryBuilder().build();
    }
    final String yearFrom = criteria.getSearchTerm().getYearFrom();
    final String fuelType = criteria.getSearchTerm().getFuelType();

    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.ID_MAKE.fullQField(), idMake));
    queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.ID_MODEL.fullQField(), idModel));
    if (StringUtils.isNoneBlank(yearFrom)) {
      Integer yearMonthFrom = VehicleQueryUtils.getFormattedBuiltYearAndDecMonth(yearFrom);
      queryBuilder.must(QueryBuilders.rangeQuery(Index.Vehicle.SEARCH_BUILT_YEAR_FROM.field())
          .lte(yearMonthFrom));
      Integer yearMonthTill = VehicleQueryUtils.getFormattedBuiltYearAndJanMonth(yearFrom);
      queryBuilder.must(QueryBuilders.rangeQuery(Index.Vehicle.SEARCH_BUILT_YEAR_TILL.field())
          .gte(yearMonthTill));
    }
    if (StringUtils.isNoneBlank(fuelType)) {
      queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.VEHICLE_FUEL_TYPE_RAW.field(), fuelType));
    }

    final VehicleFilteringCriteria filtering = criteria.getFiltering();
    if (Objects.nonNull(filtering)) {
      VehicleQueryUtils.getFilteringQueries(criteria.getFiltering()).forEach(queryBuilder::must);
    }

    log.debug("Query = {}", queryBuilder);

    final NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
        .withPageable(defaultPageable(pageable))
        .withIndices(indices)
        .withQuery(queryBuilder);

    final VehicleSearchSortCriteria sort = criteria.getSort();
    if (Objects.nonNull(sort)) {
      VehicleQueryUtils.getSortingQueries(sort).forEach(searchQueryBuilder::withSort);
    }

    if (criteria.isAggregation()) {
      aggregated(searchQueryBuilder, AGGREGATION_VEHICLE_FILTERING_FIELDS);
      getCustomAggregationQueryBuilder().forEach(searchQueryBuilder::addAggregation);
    }
    return searchQueryBuilder.build();
  }

  private List<? extends AbstractAggregationBuilder<?>> getCustomAggregationQueryBuilder() {
    return Stream.of(VehicleSearchAggregationMode.values())
        .map(VehicleSearchAggregationMode::getQueryBuilder)
        .map(IAggregationQueryBuilder::build)
        .collect(Collectors.toList());
  }
}
