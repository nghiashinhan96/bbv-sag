package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WssWorkingDayCode {
  WORKING_DAY,
  NON_WORKING_DAY,
  PUBLIC_HOLIDAY;

  public boolean isWorkingDay() {
    return this == WORKING_DAY;
  }

  public boolean isNonWorkingDay() {
    return this == NON_WORKING_DAY || this == PUBLIC_HOLIDAY;
  }
}
