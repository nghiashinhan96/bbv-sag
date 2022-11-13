package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@Slf4j
public enum PriceDisplaySelection {

  TRUE("true"), FALSE("false"), INHERIT_FROM_GLOBAL("inheritFromGlobal");

  private String value;

  public static PriceDisplaySelection fromText(final String text) {
    return Arrays.asList(values()).stream()
        .filter(val -> StringUtils.equalsIgnoreCase(text, val.getValue())).findFirst()
        .orElseGet(() -> {
          log.warn("PriceDisplaySelection {} is not supported!", text);
          return PriceDisplaySelection.INHERIT_FROM_GLOBAL;
        });
  }

}
