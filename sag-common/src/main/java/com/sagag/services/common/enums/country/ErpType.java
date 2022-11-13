package com.sagag.services.common.enums.country;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@Getter
public enum ErpType {

  NONE(StringUtils.EMPTY),
  TOP_MOTIVE(StringUtils.EMPTY),
  DYNAMIC_AX("ax"),
  WINT("wt");

  private String shortCode;

}
