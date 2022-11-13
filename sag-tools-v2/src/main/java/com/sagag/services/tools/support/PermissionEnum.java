package com.sagag.services.tools.support;

public enum PermissionEnum {
  OFFER, HOME, BULB, OIL, BATTERY, DMS, OCI, WHOLESALER, TYRE, HAYNESPRO, VIN;

  public boolean isOil() {
    return this == OIL;
  }

  public boolean isOffer() {
    return this == OFFER;
  }

  public boolean isWholesaler() {
    return this == WHOLESALER;
  }
}
