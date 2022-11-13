package com.sagag.services.ax.availability.externalvendor;

import org.apache.commons.lang3.StringUtils;

public enum AxAvailabilityType {

  VEN, CON, VWH, VWI, VWO;

  public static AxAvailabilityType from(final String type) {
    for (AxAvailabilityType axAvailabilityType : values()) {
      if (StringUtils.equalsIgnoreCase(axAvailabilityType.name(), type)) {
        return axAvailabilityType;
      }
    }
    return null;
  }

}
