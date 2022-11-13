package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SendMethodType {

  TOUR("shipping.tour"),
  PICKUP("shipping.pickup"),
  COURIER("shipping.courier");

  private String msgCode;

}
