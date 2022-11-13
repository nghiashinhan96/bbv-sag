package com.sagag.services.dvse.enums.unicat;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UnicatPriceType {

  UVPE(5),
  NETTO(4)
  ;

  private int priceType;

  public static String getFromType(int type) {
    return Stream.of(UnicatPriceType.values()).filter(u -> u.priceType == type)
        .map(UnicatPriceType::name).findFirst().orElse(null);
  }

}
