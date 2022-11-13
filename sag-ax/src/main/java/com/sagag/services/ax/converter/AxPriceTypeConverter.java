package com.sagag.services.ax.converter;

import java.util.Optional;

import com.sagag.services.common.enums.PriceEnum;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AxPriceTypeConverter {

  public static String apply(String priceType) {
    return Optional.ofNullable(PriceEnum.fromString(priceType)).map(PriceEnum::getAxId)
        .orElse(null);
  }
}
