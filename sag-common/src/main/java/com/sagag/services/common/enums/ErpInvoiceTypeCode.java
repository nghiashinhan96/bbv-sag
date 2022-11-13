package com.sagag.services.common.enums;

import java.util.Arrays;

public enum ErpInvoiceTypeCode {

  SINGLE_INVOICE,
  TWO_WEEKLY_INVOICE,
  WEEKLY_INVOICE,
  DAILY_INVOICE,
  ACCUMULATIVE_INVOICE,
  MONTHLY_INVOICE,
  WEEKLY_INVOICE_WITH_CREDIT_SEPARATION,
  DAILY_INVOICE_WITH_CREDIT_SEPARATION,
  MONTHLY_INVOICE_WITH_CREDIT_SEPARATION,
  SINGLE_INVOICE_WITH_CREDIT_SEPARATION,
  TWO_WEEKLY_INVOICE_WITH_CREDIT_SEPARATION,
  MONTHLY_INVOICE_WEEKLY_CREDIT_SEPARATION,
  DEFAULT,
  TWO_WEEKLY_INVOICE_DAILY_CREDIT,
  MONTHLY_INVOICE_DAILY_CREDIT,
  WEEKLY_INVOICE_SINGLE_CREDIT,
  WEEKLY_INVOICE_DAILY_CREDIT,
  ALL;

  /**
   * Check if invoiceTypeCode is contained in this enum.
   *
   * @param invoiceTypeCode the invoiceTypeCode to check
   * @return true if invoiceTypeCode existed, otherwise false
   */
  public static boolean contains(String invoiceTypeCode) {
    return Arrays.stream(ErpInvoiceTypeCode.values()).anyMatch(
        e -> e.name().equals(invoiceTypeCode));
  }

}
