package com.sagag.services.ax.enums.wint;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.sagag.services.ax.exception.AxCustomerException;

import lombok.Getter;

@Getter
public enum WtSendMethod {
  PICKUP("PICKUP"),
  TOUR("TOUR");

  private String code;

  private static Map<String, WtSendMethod> map = new HashMap<>();

  static {
    for (WtSendMethod type : WtSendMethod.values()) {
      map.put(type.code, type);
    }
  }

  private WtSendMethod(final String code) {
    this.code = code;
  }

  public static WtSendMethod findByCode(final String code) {
    return Optional.ofNullable(map.get(StringUtils.defaultString(code).toUpperCase()))
        .orElseThrow(() -> new AxCustomerException(
            AxCustomerException.AxCustomerErrorCase.AC_SEND_METHOD_002,
            String.format("No support this send method = %s", code)));
  }
}
