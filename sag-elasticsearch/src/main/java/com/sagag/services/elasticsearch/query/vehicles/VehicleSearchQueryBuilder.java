package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.elasticsearch.criteria.vehicle.VehicleFilteringCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchSortCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.IAggregationQueryBuilder;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import com.sagag.services.elasticsearch.query.vehicles.builder.AbstractVehicleSearchTermQueryBuilder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Slf4j
public class VehicleSearchQueryBuilder implements ISearchQueryBuilder<VehicleSearchCriteria>, IAggregationBuilder {

  private static final Index.Vehicle[] AGGREGATION_VEHICLE_FILTERING_FIELDS = {
      Index.Vehicle.VEHICLE_BUILT_YEAR_FROM,
      Index.Vehicle.VEHICLE_BODY_TYPE_RAW,
      Index.Vehicle.VEHICLE_FUEL_TYPE_RAW,
      Index.Vehicle.VEHICLE_ZYLINDER,
      Index.Vehicle.VEHICLE_ENGINE,
      Index.Vehicle.VEHICLE_CAPACITY_CC_TECH,
      Index.Vehicle.VEHICLE_ENGINE_CODE_RAW,
      Index.Vehicle.VEHICLE_DRIVE_TYPE_RAW
    };

  @Autowired
  private List<AbstractVehicleSearchTermQueryBuilder> searchTermQueryBuilders;

  @Override
  public SearchQuery buildQuery(final VehicleSearchCriteria criteria, final Pageable pageable,
      final String... indices) {
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.notEmpty(indices, "The given indices must not be empty");

    final BoolQueryBuilder query = QueryBuilders.boolQuery();
    query.must(commonQueryBuilder(criteria.getSearchTerm()));

    final VehicleFilteringCriteria filtering = criteria.getFiltering();
    if (filtering != null) {
      VehicleQueryUtils.getFilteringQueries(criteria.getFiltering()).forEach(query::must);
    }

    log.debug("Query = {}", query);

    final NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
        .withPageable(defaultPageable(pageable))
        .withIndices(indices)
        .withQuery(query);

    final VehicleSearchSortCriteria sort = criteria.getSort();
    if (sort != null) {
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

  private QueryBuilder commonQueryBuilder(final VehicleSearchTermCriteria searchTerm) {
    return searchTermQueryBuilders.stream().filter(builder -> builder.isValid(searchTerm))
        .findFirst().map(queryBuilder -> queryBuilder.build(searchTerm))
        .orElseThrow(() -> new IllegalArgumentException("Can not find the compatible search mode"));
  }
}
