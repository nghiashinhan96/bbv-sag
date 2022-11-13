package com.sagag.services.common.enums;

import java.util.Arrays;

public enum OrderStatus {
  OFFER_CONVERTED,
  INVOICED,
  REVERSAL,
  STANDING_ORDER_EXPIRED,
  CAPTURING_FINISHED,
  OFFER_EXPIRED,
  CREDITED,
  CANCELLED,
  GENERATED,
  OFFER_PRINTED,
  PARTLY_INVOICED;

  /**
   * Check if orderStatus is contained in this enum.
   *
   * @param orderStatus the invoiceTypeCode to check
   * @return true if invoiceTypeCode existed, otherwise false
   */
  public static boolean contains(String orderStatus) {
    return Arrays.stream(OrderStatus.values()).anyMatch(
        e -> e.name().equals(orderStatus));
  }
}
