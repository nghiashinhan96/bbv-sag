package com.sagag.services.common.enums;

public enum WssDeliveryProfileDay {
  ALL(0), MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

  private final int value;

  private WssDeliveryProfileDay(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
