package com.sagag.services.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum WeekDay {
  MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

  private final int value;

  WeekDay(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static WeekDay findByValue(int value) {
    for (WeekDay v : values()) {
      if (v.value == value) {
        return v;
      }
    }
    return null;
  }

  public static List<WeekDay> buildNextWeekDayList(WeekDay currentWeekDay) {
    List<WeekDay> weekDays = new ArrayList<>();
    WeekDay checkingWeekDay = findNextWeekDay(currentWeekDay);
    while (!weekDays.contains(currentWeekDay)) {
      weekDays.add(checkingWeekDay);
      checkingWeekDay = findNextWeekDay(checkingWeekDay);
    }
    return weekDays;
  }

  public static WeekDay findNextWeekDay(WeekDay currentWeekDay) {
    if (currentWeekDay == null) {
      return null;
    }
    if (SUNDAY.equals(currentWeekDay)) {
      return MONDAY;
    }
    return findByValue(currentWeekDay.getValue() + 1);
  }

}
