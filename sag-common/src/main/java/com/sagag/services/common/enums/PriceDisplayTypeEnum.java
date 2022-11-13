package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.ArrayUtils;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum PriceDisplayTypeEnum {

  UVPE_OEP_GROSS(
      ArrayUtils.toArray(PriceEnum.UVPE, PriceEnum.OEP, PriceEnum.GROSS)),
  OEP_UVPE_GROSS(
      ArrayUtils.toArray(PriceEnum.OEP, PriceEnum.UVPE, PriceEnum.GROSS)),
  UVPE_OEP(ArrayUtils.toArray(PriceEnum.UVPE, PriceEnum.OEP)),
  DPC_GROSS(ArrayUtils.toArray(PriceEnum.DPC, PriceEnum.GROSS)),
  DPC(ArrayUtils.toArray(PriceEnum.DPC));

  private PriceEnum[] priceDisplayPriority;

  public static PriceDisplayTypeEnum defaultValueOf(String priceDisplayType) {
    return Stream.of(values()).filter(val -> val.name().equalsIgnoreCase(priceDisplayType))
        .findFirst().orElse(UVPE_OEP_GROSS);
  }

  public String[] convertToArray() {
    return Stream.of(priceDisplayPriority).map(PriceEnum::name).toArray(String[]::new);
  }
}
