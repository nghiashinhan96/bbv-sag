package com.sagag.services.tools.support;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryType {

  PICKUP(1), TOUR(2), STOCK(3), OTHER(4);
  
  private int id;
  
  public static int idOf(String type) {
    DeliveryType deliveryType = Stream.of(values())
        .filter(value -> StringUtils.equalsIgnoreCase(value.name(), type))
        .findFirst().orElse(null);
    return deliveryType == null ? OTHER.getId() : deliveryType.getId();
  }
  
}
