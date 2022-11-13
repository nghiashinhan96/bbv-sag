package com.sagag.services.common.utils;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class AutonetCompanyUtils {

  private static final String AUTONET_PATTERN_PREFIX = "autonet";

  public boolean isAutonetEndpoint(String affiliate) {
    return StringUtils.startsWith(affiliate, AUTONET_PATTERN_PREFIX);
  }

}
