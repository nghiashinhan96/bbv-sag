package com.sagag.services.domain.eshop.common;

import org.apache.commons.lang3.StringUtils;

public enum AvailabilityDisplayState {

  SAME_DAY,
  NEXT_DAY,
  PARTIALLY_AVAILABLE,
  NOT_ORDERABLE,
  NOT_AVAILABLE,
  IN_STOCK;

  public  boolean isNotAvailable() {
    return this == NOT_AVAILABLE;
  }

  public  boolean isNotOrderableState() {
    return this == NOT_ORDERABLE;
  }

  public static AvailabilityDisplayState valueOfSafely(String state) {
    if (StringUtils.isBlank(state)) {
      return null;
    }
    return AvailabilityDisplayState.valueOf(state);
  }
}
