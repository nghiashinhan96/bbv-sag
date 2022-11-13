package com.sagag.services.gtmotive.domain.response;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public enum VehicleItemEnum {

  MODEL("CAR"), ENGINE("MOT"), TRANSMISSION("TRD");

  @Getter
  private String family;

  public static VehicleItemEnum valueOfFamilyCode(String family) {
    return Stream.of(values()).filter(value -> StringUtils.equals(value.getFamily(), family))
      .findFirst().orElse(null);
  }

  public static boolean contains(String family) {
    return valueOfFamilyCode(family) != null;
  }

}
