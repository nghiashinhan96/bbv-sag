package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.enums.TransType;
import com.sagag.services.elasticsearch.enums.VehicleCodeAttribute;
import com.sagag.services.elasticsearch.query.vehicles.VehicleSearchMode;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

@Component
public class VehicleCodesQueryBuilder extends VehicleDataQueryBuilder {

  @Override
  public QueryBuilder build(VehicleSearchTermCriteria criteria) {
    final String[] vehCodes = criteria.getVehicleCodes().stream().toArray(String[]::new);
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
        .should(vehicleDataQueryBuilder(VehicleCodeAttribute.TYPENSCHEIN, TransType.CH, vehCodes));
    return QueryBuilders.nestedQuery(Index.Vehicle.VEH_CODE.path(), queryBuilder,
        ScoreMode.None);
  }

  @Override
  public VehicleSearchMode mode() {
    return VehicleSearchMode.VEHICLE_CODES;
  }

  @Override
  public boolean isValid(VehicleSearchTermCriteria criteria) {
    return criteria != null && !CollectionUtils.isEmpty(criteria.getVehicleCodes());
  }

}
