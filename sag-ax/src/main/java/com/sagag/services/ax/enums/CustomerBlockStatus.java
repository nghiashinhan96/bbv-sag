package com.sagag.services.ax.enums;

import java.util.stream.Stream;

/**
 * Enumeration class to define customer block status from AX.
 *
 */
public enum CustomerBlockStatus {
  NONE, NEVER, ALL, PAYMENT, PROCUREMENT, INVOICE, DELIVERY, INVOICE_AND_DELIVERY;

  /**
   * Checks customer block status is true or not.
   *
   * <pre>
   * Business Services mapping
   * =====================
   * NONE: False
   * NEVER: False
   * ALL: True
   * PAYMENT: False
   * PROCUREMENT: True
   * INVOICE: True
   * DELIVERY: True
   * INVOICE_AND_DELIVERY: True
   * </pre>
   *
   * @return <code></code> if it's blocked, otherwise
   */
  public boolean isBlocked() {
    return Stream.of(ALL, PROCUREMENT, INVOICE, DELIVERY, INVOICE_AND_DELIVERY)
        .anyMatch(status -> status == this);
  }
}
