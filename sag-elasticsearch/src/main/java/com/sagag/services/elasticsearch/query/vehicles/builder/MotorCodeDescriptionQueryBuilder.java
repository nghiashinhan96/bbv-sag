package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.vehicles.VehicleSearchMode;
import com.sagag.services.elasticsearch.utils.QueryUtils;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class MotorCodeDescriptionQueryBuilder extends AbstractVehicleSearchTermQueryBuilder {

  private static final String[] SEARCH_VEHICLE_MOTOR_CODE_AND_DESC_FIELDS = new String[] {
          Index.Vehicle.VEHICLE_ENGINE_CODE.field(),
          Index.Vehicle.VEHICLE_ENGINE_CODE_FULL.field(),
          Index.Vehicle.VEHICLE_BRAND.field(),
          Index.Vehicle.VEHICLE_MODEL.field(),
          Index.Vehicle.VEHICLE_NAME.field(),
          Index.Vehicle.MODEL.field() };

  @Override
  public QueryBuilder build(VehicleSearchTermCriteria criteria) {
    return QueryUtils.multiMatchQueryBuilder(QueryUtils.removeNonAlphaChars(
      criteria.getMotorCodeDesc(), true), SEARCH_VEHICLE_MOTOR_CODE_AND_DESC_FIELDS, attributeBoosts());
  }

  @Override
  public VehicleSearchMode mode() {
    return VehicleSearchMode.MOTOR_CODE_DESC;
  }

  @Override
  public boolean isValid(VehicleSearchTermCriteria criteria) {
    return criteria != null && !StringUtils.isBlank(criteria.getMotorCodeDesc());
  }

}
