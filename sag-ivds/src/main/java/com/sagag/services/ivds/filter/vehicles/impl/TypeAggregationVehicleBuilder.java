package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.domain.eshop.dto.TypeItem;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleBuilder;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TypeAggregationVehicleBuilder implements AggregationVehicleBuilder {

  @Autowired
  private VehicleSearchService vehicleSearchService;

  @Override
  public Map<AggregationVehicleMode, List<Object>> buildAggregation(VehicleMakeModelTypeSearchRequest request) {
    Assert.hasText(request.getMakeCode(), "The given make code must not be empty");
    Assert.hasText(request.getModelCode(), "The given model code must not be empty");
    final int makeId = Integer.parseInt(request.getMakeCode());
    final int modelId = Integer.parseInt(request.getModelCode());
    final List<VehicleDoc> vehicles = vehicleSearchService.searchTypesByMakeModel(makeId, modelId,
      request.getYearFrom(), request.getFuelType());
    final List<TypeItem> typeItems = vehicles.stream().map(typeItemConverter())
      .sorted((item, compareItem) -> {
        if (item.getSort() == null) {
          return -1; // move to the end
        }
        return item.getSort().compareTo(compareItem.getSort());
      }).collect(Collectors.toList());

    final List<String> fuelItems = vehicles.stream().map(VehicleDoc::getVehicleFuelType).distinct()
      .collect(Collectors.toList());
    final Map<AggregationVehicleMode, List<Object>> map =
        new EnumMap<>(AggregationVehicleMode.class);
    map.put(AggregationVehicleMode.TYPE, Collections.unmodifiableList(typeItems));
    map.put(AggregationVehicleMode.FUEL, Collections.unmodifiableList(fuelItems));
    return map;
  }

  private static Function<VehicleDoc, TypeItem> typeItemConverter() {
    return vehDoc -> TypeItem.builder()
      .vehId(vehDoc.getVehId())
      .vehicleName(vehDoc.getVehicleName())
      .vehicleEngine(vehDoc.getVehicleEngine())
      .vehiclePowerKw(vehDoc.getVehiclePowerKw())
      .vehicleEngineCode(vehDoc.getVehicleEngineCode())
      .vehicleCapacityCcTech(vehDoc.getVehicleCapacityCcTech())
      .vehiclePowerHp(vehDoc.getVehiclePowerHp())
      .sort(vehDoc.getSort()).build();
  }

  @Override
  public boolean isValid(VehicleMakeModelTypeSearchRequest request) {
    return request.isRequestTypeAgg();
  }
}
