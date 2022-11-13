package com.sagag.services.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

public enum PermissionEnum {
  NONE, OFFER, HOME, BULB, OIL, BATTERY, DMS, OCI, WHOLESALER, TYRE, HAYNESPRO, VIN, UNIPARTS, DVSE, MOTO;

  public boolean isOil() {
    return this == OIL;
  }

  public boolean isOffer() {
    return this == OFFER;
  }

  public boolean isWholesaler() {
    return this == WHOLESALER;
  }

  public boolean isDvse() {
    return this == DVSE;
  }

  public static PermissionEnum valueOfSafely(String permission) {
    return Stream.of(values()).filter(e -> StringUtils.equalsIgnoreCase(permission, e.name()))
        .findFirst().orElse(NONE);
  }
}
