package com.sagag.services.common.utils;

import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.util.Arrays;

@UtilityClass
public final class VehicleUtils {

  private static final String KW = "kW";

  private static final char MOTOR = 'M';

  private static final char KTYPE = 'V';

  public static final String KTYPE_OPEN = String.valueOf(KTYPE);

  public static final String KTYPE_CLOSE = String.valueOf(MOTOR);

  public static String buildVehicleInfo(final String vehBrand, final String vehModel,
      final String vehName, final String vehPowerKw, final String vehEngCode) {
    return StringUtils.join(new String[] {
        vehBrand,
        vehModel,
        buildVehicleTypeDesc(vehName, vehPowerKw, vehEngCode)
      }, SagConstants.SPACE);
  }

  public static String buildVehicleTypeDesc(final String vehName, final String vehPowerKw,
      final String vehEngCode) {

    final String displayedPowerKw = StringUtils.isBlank(vehPowerKw) ? StringUtils.EMPTY
        : new StringBuilder(vehPowerKw).append(SagConstants.SPACE).append(KW).toString();

    return new StringBuilder().append(StringUtils.defaultString(vehName)).append(SagConstants.SPACE)
        .append(StringUtils.defaultString(displayedPowerKw)).append(SagConstants.SPACE)
        .append(StringUtils.defaultString(vehEngCode)).toString();
  }

  /**
   * Returns the vehicle display information.
   *
   * @param vehBrand the vehicle brand
   * @param model the vehicle model
   * @param typeDesc the vehicle type description
   * @return the vehicle display information
   */
  public static String buildVehDisplay(final String vehBrand, final String model,
    final String typeDesc) {
    return StringUtils.join(Arrays.array(vehBrand, model, typeDesc), StringUtils.SPACE);
  }

  public static String buildVehicleId(final String kType, final String motorNrs) {
    return KTYPE + kType + MOTOR + StringUtils.defaultIfBlank(motorNrs,
        NumberUtils.INTEGER_ZERO.toString());
  }

  public String[] analyzeVehicleId(final String vehicleId) {
    if (StringUtils.isBlank(vehicleId)) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    return vehicleId.split(String.valueOf(MOTOR));
  }

  public String getKTypeNr(String vehicleId) {
    if (vehicleId.equals(SagConstants.KEY_NO_VEHICLE)) {
      return StringUtils.EMPTY;
    }
    final String[] vehicleInfos = StringUtils.split(vehicleId, MOTOR);
    final String[] ktype = StringUtils.split(vehicleInfos[0], KTYPE);
    return ktype.length < 2 ? StringUtils.EMPTY : ktype[1];
  }

  public String extractKTypeValue(String kTypeNr) {
    if (StringUtils.isBlank(kTypeNr)) {
      return StringUtils.EMPTY;
    }
    final String[] ktype = kTypeNr.split(String.valueOf(VehicleUtils.KTYPE));
    return ktype.length < 2 ? StringUtils.EMPTY : ktype[1];
  }

  public String getMotorNrs(String vehicleId) {
    if (StringUtils.isBlank(vehicleId) || vehicleId.equals(SagConstants.KEY_NO_VEHICLE)) {
      return StringUtils.EMPTY;
    }
    String[] vehicleInfos = StringUtils.split(vehicleId, MOTOR);
    if (vehicleInfos.length < 2) {
      return StringUtils.EMPTY;
    }
    if (vehicleInfos[1].equals(NumberUtils.INTEGER_ZERO.toString())) {
      return StringUtils.EMPTY;
    }
    return vehicleInfos[1];
  }

  public String extractMotorValues(String motor) {
    if (StringUtils.isBlank(motor) || motor.equals(NumberUtils.INTEGER_ZERO.toString())) {
      return StringUtils.EMPTY;
    }
    return motor;
  }

  public static String extractKTypeFromVehId(String vehicleId) {
    return StringUtils.substringBetween(vehicleId, KTYPE_OPEN, KTYPE_CLOSE);
  }

  public static String defaultVehId(String vehId) {
    return StringUtils.defaultIfBlank(vehId, SagConstants.KEY_NO_VEHICLE);
  }

  public static boolean isValidVehId(String vehId) {
    return !StringUtils.isBlank(vehId)
        && !StringUtils.equalsIgnoreCase(vehId, SagConstants.KEY_NO_VEHICLE);
  }
}
