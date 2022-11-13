package com.sagag.services.tools.batch.migration.customer_settings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.ArrayUtils;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum PriceDisplayTypeEnum {
  UVPE_OEP_GROSS(
      ArrayUtils.toArray(PriceEnum.UVPE, PriceEnum.OEP, PriceEnum.GROSS)), OEP_UVPE_GROSS(
          ArrayUtils.toArray(PriceEnum.OEP, PriceEnum.UVPE, PriceEnum.GROSS));

  private PriceEnum[] priceDisplayPriority;

  public static PriceDisplayTypeEnum defaultValueOf(String priceDisplayType) {
    return Stream.of(values()).filter(val -> val.name().equalsIgnoreCase(priceDisplayType))
        .findFirst().orElse(UVPE_OEP_GROSS);
  }

  public String[] convertToArray() {
    return Stream.of(priceDisplayPriority).map(PriceEnum::name).toArray(String[]::new);
  }
}
