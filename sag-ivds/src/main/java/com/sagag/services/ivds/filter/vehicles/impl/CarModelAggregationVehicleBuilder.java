package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CarModelAggregationVehicleBuilder extends ModelAggregationVehicleBuilder {


  @Override
  public Map<AggregationVehicleMode, List<Object>> buildAggregation(
      VehicleMakeModelTypeSearchRequest request) {
    return super.buildAggregation(request);
  }

  @Override
  public boolean isValid(VehicleMakeModelTypeSearchRequest request) {
    return request.isRequestModelAggForCar();
  }

}
