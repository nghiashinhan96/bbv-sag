package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryMethodType {

  URGENT("delivery.urgent"),
  NORMAL("delivery.normal");

  private String msgCode;

}
