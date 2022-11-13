package com.sagag.services.article.api.price.finalcustomer;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

public interface WssMarginValueFinder {

  double findMarginValue(Integer orgId, Integer marginGroupNumber, Integer brandId,
      List<String> articleGroups);

  default double defaultMarginValue(Double marginValue) {
    return marginValue != null ? marginValue.doubleValue() : NumberUtils.DOUBLE_ZERO.doubleValue();
  }

}
