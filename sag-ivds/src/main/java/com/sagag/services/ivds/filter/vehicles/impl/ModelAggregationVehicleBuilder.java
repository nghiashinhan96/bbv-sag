package com.sagag.services.ivds.filter.vehicles.impl;

import static java.util.stream.Collectors.groupingBy;

import com.sagag.services.domain.eshop.dto.AdvanceVehicleModel;
import com.sagag.services.domain.eshop.dto.ModelItem;
import com.sagag.services.hazelcast.api.ModelCacheService;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleBuilder;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public abstract class ModelAggregationVehicleBuilder implements AggregationVehicleBuilder {

  @Autowired
  protected ModelCacheService modelCacheService;

  @Override
  public Map<AggregationVehicleMode, List<Object>> buildAggregation(
      VehicleMakeModelTypeSearchRequest request) {
    Assert.hasText(request.getMakeCode(), "The given make code must not be empty");
    final Integer makeId = Integer.parseInt(request.getMakeCode());
    final List<ModelItem> models = modelCacheService.getAllSortedModelsByMake(makeId,
        request.getVehicleType(), Collections.emptyList(), request.getYearFrom());
    final List<Integer> fromYears =
        models.stream().map(ModelItem::getModelDateBegin).distinct().collect(Collectors.toList());

    final Map<AggregationVehicleMode, List<Object>> map =
        new EnumMap<>(AggregationVehicleMode.class);
    map.put(AggregationVehicleMode.MODEL, Collections.unmodifiableList(models));
    map.put(AggregationVehicleMode.YEAR, Collections.unmodifiableList(fromYears));
    return map;
  }

  @Override
  public AdvanceVehicleModel buildAdvanceAggregation(VehicleMakeModelTypeSearchRequest request) {
    Assert.hasText(request.getMakeCode(), "The given make code must not be empty");
    final Integer makeId = Integer.parseInt(request.getMakeCode());
    final List<ModelItem> models = modelCacheService.getAllSortedModelsByMake(makeId,
        request.getVehicleType(), Collections.emptyList(), request.getYearFrom());

    List<ModelItem> validItems = models.stream().filter(item -> Objects.nonNull(item.getModelSeries()))
        .collect(Collectors.toList());

    return AdvanceVehicleModel.builder()
        .models(buildModelItems(validItems))
        .years(buildYearFiltering(validItems))
        .build();
  }

  private Map<String, List<ModelItem>> buildModelItems(List<ModelItem> models) {
    return models.stream()
        .collect(groupingBy(ModelItem::getModelSeries,
            Collectors.mapping(Function.identity(), Collectors.collectingAndThen(Collectors.toList(),
                e -> e.stream().sorted(Comparator.comparing(ModelItem::getModel)).collect(Collectors.toList())))));
  }

  private List<Integer> buildYearFiltering(List<ModelItem> models) {
    return models.stream().flatMap(model -> Stream.of(model.getModelDateBegin(), model.getModelDateEnd()))
        .distinct()
        .filter(year -> !year.equals(0))
        .sorted()
        .collect(Collectors.toList());
  }
}
