package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.hazelcast.api.MakeCacheService;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleBuilder;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class MakeAggregationVehicleBuilder implements AggregationVehicleBuilder {

  @Autowired
  protected MakeCacheService makeCacheService;

  @Override
  public Map<AggregationVehicleMode, List<Object>> buildAggregation(
      VehicleMakeModelTypeSearchRequest request) {
    final Map<AggregationVehicleMode, List<Object>> map =
        new EnumMap<>(AggregationVehicleMode.class);
    map.put(AggregationVehicleMode.MAKE, Collections.unmodifiableList(
      makeCacheService.getAllSortedMakes(request.getVehicleType(), request.getAffiliate())));
    return map;
  }

}
