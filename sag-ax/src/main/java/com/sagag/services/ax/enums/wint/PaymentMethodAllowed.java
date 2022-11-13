package com.sagag.services.ax.enums.wint;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.sagag.services.ax.exception.AxCustomerException;

import lombok.Getter;

@Getter
public enum PaymentMethodAllowed {
  CASH_ONLY("Cash Payment", new WtPaymentType[] { WtPaymentType.CASH }),
  WHOLESALE("Wholesale", new WtPaymentType[] { WtPaymentType.WHOLESALE }),
  WHOLESALE_AND_CASH("Wholesale and Cash Payment", new WtPaymentType[] { WtPaymentType.CASH, WtPaymentType.WHOLESALE});

  private String code;

  WtPaymentType[] allowsPayments;

  private static Map<String, PaymentMethodAllowed> map = new HashMap<>();

  static {
    for (PaymentMethodAllowed type : PaymentMethodAllowed.values()) {
      map.put(type.code.toLowerCase(), type);
    }
  }

  private PaymentMethodAllowed(final String code, final WtPaymentType[] allowsPayments) {
    this.code = code;
    this.allowsPayments = allowsPayments;
  }

  public static PaymentMethodAllowed findByCode(final String code) {
    return Optional.ofNullable(map.get(StringUtils.defaultString(code).toLowerCase()))
        .orElseThrow(() -> new AxCustomerException(
            AxCustomerException.AxCustomerErrorCase.AC_PAYMENT_TYPE_002,
            String.format("No support this PaymentMethodAllowed = %s", code)));
  }

}
