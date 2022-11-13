package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.domain.eshop.dto.CupicCapacityVehicle;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleBuilder;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CubicCapacityAggregationVehicleBuilder implements AggregationVehicleBuilder {

  @Autowired
  private VehicleSearchService vehicleSearchService;

  @Override
  public Map<AggregationVehicleMode, List<Object>> buildAggregation(
      VehicleMakeModelTypeSearchRequest request) {

    final int makeId = Integer.parseInt(request.getMakeCode());

    final Map<AggregationVehicleMode, List<Object>> map =
        new EnumMap<>(AggregationVehicleMode.class);

    List<VehicleDoc> vehicles = vehicleSearchService
        .searchVehiclesByMakeModelForSpecificVehicleType(request.getVehicleType(),
            request.getVehicleSubClass(), makeId, null, StringUtils.EMPTY, StringUtils.EMPTY);
    List<CupicCapacityVehicle> cupicCapacity =
        CollectionUtils.emptyIfNull(vehicles).stream().map(cupicConverter()).distinct()
            .sorted(Comparator.comparing(CupicCapacityVehicle::getCapacity))
            .collect(Collectors.toList());
    map.put(AggregationVehicleMode.CUBIC_CAPACITY, Collections.unmodifiableList(cupicCapacity));
    return map;
  }

  private Function<VehicleDoc, CupicCapacityVehicle> cupicConverter() {
    return vehicleDoc -> CupicCapacityVehicle.builder()
        .capacity(vehicleDoc.getVehicleCapacityCcTech()).build();
  }

  @Override
  public boolean isValid(VehicleMakeModelTypeSearchRequest request) {
    return request.isRequestCupicCapacityAgg();
  }

}
