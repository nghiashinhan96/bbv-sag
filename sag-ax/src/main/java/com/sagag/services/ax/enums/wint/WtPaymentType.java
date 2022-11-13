package com.sagag.services.ax.enums.wint;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.sagag.services.ax.exception.AxCustomerException;

import lombok.Getter;

@Getter
public enum WtPaymentType {

  CASH("CASH"),
  WHOLESALE("WHOLESALE");

  private String code;

  private static Map<String, WtPaymentType> map = new HashMap<>();

  static {
    for (WtPaymentType type : WtPaymentType.values()) {
      map.put(type.code, type);
    }
  }

  private WtPaymentType(final String code) {
    this.code = code;
  }

  public static WtPaymentType findByCode(final String code) {
    return Optional.ofNullable(map.get(StringUtils.defaultString(code).toUpperCase()))
        .orElseThrow(() -> new AxCustomerException(
            AxCustomerException.AxCustomerErrorCase.AC_PAYMENT_TYPE_002,
            String.format("No support this payment type = %s", code)));
  }
}
