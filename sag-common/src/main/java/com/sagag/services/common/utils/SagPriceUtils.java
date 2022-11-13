package com.sagag.services.common.utils;

import com.sagag.services.common.contants.SagConstants;
import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.util.Objects;

@UtilityClass
public final class SagPriceUtils {

  public static final String GROSS_PRICE_TYPE = "GROSS";

  /**
   * Round half even to 2 digits (#842)
   */
  public static String roundHalfEvenTo2digits(Double number) {
    if (Objects.isNull(number)) {
      return null;
    }
    Double tmp = number * 100;
    Double floor = Math.floor(tmp);
    Double ceil = Math.ceil(tmp);
    DecimalFormat df = new DecimalFormat("0.00");
    if (tmp - floor < 0.5) {
      return df.format(floor / 100);
    }
    if (ceil - tmp < 0.5) {
      return df.format(ceil / 100);
    }
    return df.format((floor % 2 == 0) ? floor / 100 : ceil / 100);
  }

  public static Double roundHalfEventTo2digitsDouble(Double number) {
    return Double.valueOf(roundHalfEvenTo2digits(number));
  }

  public double calculateVATPrice(double price, double vat) {
    return (1 + SagConstants.PERCENT * vat) * price;
  }
}
