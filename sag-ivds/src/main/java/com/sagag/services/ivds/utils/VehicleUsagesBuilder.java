package com.sagag.services.ivds.utils;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.eshop.dto.VehicleUsageDto;
import com.sagag.services.domain.eshop.dto.VehicleUsageItemDto;
import com.sagag.services.elasticsearch.domain.article.ArticleVehicles;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleUsage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VehicleUsagesBuilder {

  @NonNull
  private List<ArticleVehicles> articleVehicles;

  /**
   * Returns a list of vehicle usages ordering by vehicle display name and grouping by vehicle branch
   *
   * @return the list of vehicle usage
   */
  @LogExecutionTime
  public List<VehicleUsageDto> build() {
    if (CollectionUtils.isEmpty(articleVehicles)) {
      return Collections.emptyList();
    }

    final Map<String, List<VehicleUsageItemDto>> usageMaps = buildUsageMaps(articleVehicles);
    if (MapUtils.isEmpty(usageMaps)) {
      return Collections.emptyList();
    }
    return usageMaps.entrySet().stream()
      .map(vehicleUsageConverter())
      .sorted(vehicleUsageItemComparator())
      .collect(Collectors.toList());
  }

  private static Map<String, List<VehicleUsageItemDto>> buildUsageMaps(
    final List<ArticleVehicles> articleVehicles) {
    return articleVehicles.stream()
      .flatMap(artVehicle -> artVehicle.getVehicles().stream())
      .map(vehicleUsageItemConverter())
      .collect(Collectors.groupingBy(VehicleUsageItemDto::getVehicleBrand));
  }

  private static Function<VehicleUsage, VehicleUsageItemDto> vehicleUsageItemConverter() {
    return vehUsage -> {
      String vehId = vehUsage.getVehId();
      String vehBrand = vehUsage.getVehicleBrand();
      String vehDisplayName = vehUsage.getVehicleDisplayName();
      int sort = vehUsage.getSort();
      return new VehicleUsageItemDto(vehId, vehDisplayName, vehBrand, sort);
    };
  }

  private static Function<Map.Entry<String, List<VehicleUsageItemDto>>, VehicleUsageDto> vehicleUsageConverter() {
    return entry -> VehicleUsageDto.builder().vehicleBrand(entry.getKey())
      .vehicleUsage(entry.getValue()).build();
  }

  private static Comparator<VehicleUsageDto> vehicleUsageItemComparator() {
    return (item1, item2) -> {
      if (StringUtils.isBlank(item1.getVehicleBrand())) {
        return -1;
      }
      return item1.getVehicleBrand().compareTo(item2.getVehicleBrand());
    };
  }
}
