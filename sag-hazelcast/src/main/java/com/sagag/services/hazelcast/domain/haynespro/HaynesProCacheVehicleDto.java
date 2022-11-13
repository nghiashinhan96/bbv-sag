package com.sagag.services.hazelcast.domain.haynespro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.VehicleUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Data
@Slf4j
public class HaynesProCacheVehicleDto implements Serializable {

  private static final long serialVersionUID = -4750972456074619187L;

  private String id;
  private String name;
  private String ktypnr;
  private String motnrs;

  @JsonIgnore
  public String getLabourTimeIdAfterFilterMotorId(String motorId) {
    log.debug("Return labour time id from haynespro vehicle = {}", this);
    if (StringUtils.isEmpty(getMotnrs())) {
      log.debug("The ktypNr = {}", getKtypnr());
      return StringUtils.defaultIfBlank(getKtypnr(), StringUtils.EMPTY);
    }

    final Optional<String> selectedMotorNr =
        Stream.of(StringUtils.split(getMotnrs(), SagConstants.COMMA_NO_SPACE))
        .map(StringUtils::trim)
        .filter(filterMotorNr(motorId))
        .findFirst();
    log.debug("The found motorNr = {}", selectedMotorNr);
    return selectedMotorNr.orElse(StringUtils.EMPTY);
  }

  private static Predicate<String> filterMotorNr(final String motorId) {
    return motorNr -> StringUtils.isAnyBlank(motorNr, motorId)
        || StringUtils.equals(NumberUtils.INTEGER_ZERO.toString(), motorId)
        || StringUtils.equalsIgnoreCase(motorNr, motorId);
  }

  public static HaynesProCacheVehicleDto of(String vehicleId) {
    if (vehicleId.equals(SagConstants.KEY_NO_VEHICLE)) {
      return new HaynesProCacheVehicleDto();
    }

    final String[] vehicleInfos = VehicleUtils.analyzeVehicleId(vehicleId);
    if (vehicleInfos.length < 2) {
      return new HaynesProCacheVehicleDto();
    }

    final HaynesProCacheVehicleDto vehicle = new HaynesProCacheVehicleDto();
    vehicle.setKtypnr(VehicleUtils.extractKTypeValue(vehicleInfos[0]));
    vehicle.setMotnrs(VehicleUtils.extractMotorValues(vehicleInfos[1]));
    return vehicle;
  }
}
