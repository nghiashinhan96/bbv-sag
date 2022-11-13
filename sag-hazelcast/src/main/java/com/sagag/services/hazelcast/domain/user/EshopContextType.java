package com.sagag.services.hazelcast.domain.user;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum EshopContextType {

  VEHICLE_CONTEXT(1),
  ARTICLE_NUMBER_CONTEXT(2),
  BASKET_CONTEXT(4),
  USER_PRICE_CONTEXT(5);


  @Getter
  private int value;
  private EshopContextType(int value) {
    this.value = value;
  }


  public static EshopContextType getEshopContextType(final int value) {
    final Optional<EshopContextType> optional = Arrays.asList(values()).stream()
        .filter(type -> type.value == value).findFirst();
    if (!optional.isPresent()) {
      return null;
    }
    return optional.get();
  }
}
