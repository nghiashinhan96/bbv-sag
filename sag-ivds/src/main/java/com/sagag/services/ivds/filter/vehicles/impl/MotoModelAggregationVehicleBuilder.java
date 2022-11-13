package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.domain.eshop.dto.ModelItem;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class MotoModelAggregationVehicleBuilder extends ModelAggregationVehicleBuilder {

  @Autowired
  private VehicleSearchService vehicleSearchService;

  @Override
  public Map<AggregationVehicleMode, List<Object>> buildAggregation(
      VehicleMakeModelTypeSearchRequest request) {
    Assert.hasText(request.getMakeCode(), "The given make code must not be empty");
    final int makeId = Integer.parseInt(request.getMakeCode());

    final Map<AggregationVehicleMode, List<Object>> map =
        new EnumMap<>(AggregationVehicleMode.class);

    List<VehicleDoc> vehicles =
        vehicleSearchService.searchVehiclesByMakeModelForSpecificVehicleType(
            request.getVehicleType(), request.getVehicleSubClass(), makeId, null,
            request.getCubicCapacity(), StringUtils.EMPTY);

    List<ModelItem> returnModels = new ArrayList<>();
    CollectionUtils.emptyIfNull(vehicles).forEach(veh -> {
      String vehicleModelName = veh.getVehicleModel();
      Integer modelId = veh.getIdModel();
      returnModels.add(
          ModelItem.builder().model(vehicleModelName).modelId(modelId).sort(veh.getSort()).build());
    });

    List<ModelItem> distinctModelById = returnModels.stream()
        .filter(distinctByKey(ModelItem::getModelId))
        .sorted(Comparator.comparing(ModelItem::getSort,
            Comparator.nullsLast(Comparator.naturalOrder())))
        .collect(Collectors.toList());
    map.put(AggregationVehicleMode.MODEL, Collections.unmodifiableList(distinctModelById));
    return map;
  }
  
  @Override
  public boolean isValid(VehicleMakeModelTypeSearchRequest request) {
    return request.isRequestModelAggForMotorbike();
  }

  private static Predicate<ModelItem> distinctByKey(
      Function<ModelItem, Integer> keyExtractor) {
    Map<Integer, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }

}
