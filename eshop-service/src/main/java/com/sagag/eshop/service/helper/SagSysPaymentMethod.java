package com.sagag.eshop.service.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
@AllArgsConstructor
public enum SagSysPaymentMethod {
    CASH("B"), CREDIT("K");

  private String desc;

  /**
   * Constructs the supported SagSysPaymentMethod from its description.
   *
   * @param desc the PaymentMethod description
   * @return the supported SagSysPaymentMethod
   */
  public static SagSysPaymentMethod fromDesc(final String desc) {
    return Arrays.asList(values()).stream()
        .filter(val -> StringUtils.equals(desc, val.getDesc())).findFirst()
        .orElseThrow(() -> new NoSuchElementException("No support the given PaymentMethod"));
  }
}
