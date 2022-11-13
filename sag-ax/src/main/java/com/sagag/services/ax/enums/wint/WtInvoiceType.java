package com.sagag.services.ax.enums.wint;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.sagag.services.ax.exception.AxCustomerException;

import lombok.Getter;

@Getter
public enum WtInvoiceType {

  SINGLE("SINGLE"),
  WEEKLY("WEEKLY");

  private String code;

  private static Map<String, WtInvoiceType> map = new HashMap<>();

  static {
    for (WtInvoiceType type : WtInvoiceType.values()) {
      map.put(type.code, type);
    }
  }

  private WtInvoiceType(final String code) {
    this.code = code;
  }

  public static WtInvoiceType findByCode(final String code) {
    return Optional.ofNullable(map.get(StringUtils.defaultString(code).toUpperCase()))
        .orElseThrow(() -> new AxCustomerException(
            AxCustomerException.AxCustomerErrorCase.AC_INVOICE_TYPE_002,
            String.format("No support this invoice type = %s", code)));
  }
}
