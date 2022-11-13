package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum PriceEnum {
  UVPE("VK_APLT_UVPE"), OEP("VK_APLT_OEP"), GROSS(null), DPC("PC_DPC");

  private String axId;

  public static PriceEnum fromString(final String text) {
    return Arrays.asList(values()).stream()
        .filter(val -> StringUtils.equalsIgnoreCase(text, val.toString())).findFirst().orElse(null);
  }

}
