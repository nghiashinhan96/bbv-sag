package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkingDayCode {
  WORKING_DAY,
  NON_WORKING_DAY,
  PUBLIC_HOLIDAY;

  /**
   * Checks if the code is working day.
   *
   * @return true if the code is working day, false otherwise.
   */
  public boolean isWorkingDay() {
    return this == WORKING_DAY;
  }

  /**
   * Checks if the code is non working day.
   *
   * @return true if the code is non working day, false otherwise.
   */
  public boolean isNonWorkingDay() {
    return this == NON_WORKING_DAY || this == PUBLIC_HOLIDAY;
  }
}
