package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class CarMakeAggregationVehicleBuilder extends MakeAggregationVehicleBuilder {

  @Override
  public Map<AggregationVehicleMode, List<Object>> buildAggregation(
      VehicleMakeModelTypeSearchRequest request) {
    final Map<AggregationVehicleMode, List<Object>> map =
        new EnumMap<>(AggregationVehicleMode.class);
    map.put(AggregationVehicleMode.MAKE,
        Collections.unmodifiableList(makeCacheService.getMakesByVehicleClassAndSubClass(
            request.getVehicleType(), Collections.emptyList(), request.getAffiliate())));
    return map;
  }

  @Override
  public boolean isValid(VehicleMakeModelTypeSearchRequest request) {
    return request.isRequestMakeAggForCar();
  }

}
