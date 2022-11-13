package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.domain.eshop.dto.VehicleModelYear;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleBuilder;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;

import org.apache.commons.collections4.CollectionUtils;
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
public class YearAggregationVehicleBuilder implements AggregationVehicleBuilder {

  @Autowired
  private VehicleSearchService vehicleSearchService;

  @Override
  public Map<AggregationVehicleMode, List<Object>> buildAggregation(
      VehicleMakeModelTypeSearchRequest request) {

    final int makeId = Integer.parseInt(request.getMakeCode());
    final int modelId = Integer.parseInt(request.getModelCode());

    final Map<AggregationVehicleMode, List<Object>> map =
        new EnumMap<>(AggregationVehicleMode.class);

    List<VehicleDoc> vehicles =
        vehicleSearchService.searchVehiclesByMakeModelForSpecificVehicleType(
            request.getVehicleType(), request.getVehicleSubClass(), makeId, modelId,
            request.getCubicCapacity(), request.getYearFrom());
    List<VehicleModelYear> vehicleYears =
        CollectionUtils.emptyIfNull(vehicles).stream().map(vehicleModelYearConverter()).distinct()
            .sorted(Comparator.comparing(VehicleModelYear::getYear)).collect(Collectors.toList());
    map.put(AggregationVehicleMode.YEAR, Collections.unmodifiableList(vehicleYears));
    return map;
  }

  private Function<VehicleDoc, VehicleModelYear> vehicleModelYearConverter() {
    return vehicleDoc -> VehicleModelYear.builder()
        .year(vehicleDoc.getVehicleModelYear())
        .vehId(vehicleDoc.getId())
        .build();
  }

  @Override
  public boolean isValid(VehicleMakeModelTypeSearchRequest request) {
    return request.isRequestYearAgg();
  }

}
