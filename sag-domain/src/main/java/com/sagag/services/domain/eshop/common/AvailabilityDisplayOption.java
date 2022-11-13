package com.sagag.services.domain.eshop.common;

import org.apache.commons.lang3.StringUtils;

public enum AvailabilityDisplayOption {

  NONE,
  DOT,
  DISPLAY_TEXT,
  DROP_SHIPMENT;

  public boolean isSelectedOption() {
    return this == DOT || this == DISPLAY_TEXT || this == DROP_SHIPMENT;
  }

  public boolean isDisplayTextOption() {
    return this == NONE || this == DISPLAY_TEXT;
  }

  public boolean isDisplayTextOptionOnly() {
    return this == DISPLAY_TEXT;
  }

  public static AvailabilityDisplayOption valueOfSafely(String state) {
    if (StringUtils.isBlank(state)) {
      return null;
    }
    return AvailabilityDisplayOption.valueOf(state);
  }
}