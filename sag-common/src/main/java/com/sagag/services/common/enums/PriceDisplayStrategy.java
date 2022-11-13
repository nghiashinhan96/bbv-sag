package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@Slf4j
public enum PriceDisplayStrategy {

  NET("None", "None"),
  NET_GROSS("CalculationPrice", "Brutto + Netto"),
  NET_GROSS_DISCOUNT("CalculationPriceAndDiscount", "Brutto + Netto + Rabatt");

  private String value;
  private String uiText;

  public static PriceDisplayStrategy fromText(final String text) {
    return Arrays.asList(values()).stream()
        .filter(val -> StringUtils.equalsIgnoreCase(text, val.getValue())).findFirst()
        .orElseGet(() -> {
          log.warn("PriceDisplayStrategy {} is not supported!", text);
          return PriceDisplayStrategy.NET;
        });
  }

}
