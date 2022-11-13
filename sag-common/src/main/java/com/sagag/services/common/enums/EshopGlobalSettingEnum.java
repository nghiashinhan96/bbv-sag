package com.sagag.services.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum EshopGlobalSettingEnum {

  MOUSE_OVER_FLYOUT_DELAY,
  ENHANCED_USEDPARTS_RETURN_PROC,
  COUPON_MODULE;

  public String toValue() {
    return StringUtils.lowerCase(this.name());
  }
}
