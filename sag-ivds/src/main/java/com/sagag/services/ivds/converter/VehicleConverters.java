package com.sagag.services.ivds.converter;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

/**
 * Utility provide some converters of vehicle.
 */
@UtilityClass
public final class VehicleConverters {

  public static Function<VehicleDoc, VehicleDto> optionalVehicleConverter() {
    return vehicle -> SagBeanUtils.map(vehicle, VehicleDto.class);
  }

  public static Function<VehicleDoc, VehicleDto> simpleVehicleConverter() {
    return vehicle -> VehicleDto.builder()
        .id(vehicle.getId())
        .vehId(vehicle.getVehId())
        .vehicleFullName(vehicle.getVehicleFullName())
        .vehicleBuiltYearFrom(vehicle.getVehicleBuiltYearFrom())
        .vehicleBuiltYearTill(vehicle.getVehicleBuiltYearTill())
        .vehicleBodyType(vehicle.getVehicleBodyType())
        .vehicleFuelType(vehicle.getVehicleFuelType())
        .vehicleZylinder(vehicle.getVehicleZylinder())
        .vehicleEngine(vehicle.getVehicleEngine())
        .vehicleEngineCode(vehicle.getVehicleEngineCode())
        .vehiclePowerKw(vehicle.getVehiclePowerKw())
        .vehicleDriveType(vehicle.getVehicleDriveType())
        .vehiclePowerHp(vehicle.getVehiclePowerHp())
        .isElectric(vehicle.getIsElectric())
        .vehicleClass(vehicle.getVehicleClass())
        .vehicleName(vehicle.getVehicleName())
        .vehicleCapacityCcTech(vehicle.getVehicleCapacityCcTech())
        .build();
  }

}
