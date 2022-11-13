package com.sagag.eshop.service.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum MessageLocationTypeEnum {
  AFFILIATE, CUSTOMER;

  public static MessageLocationTypeEnum fromDesc(final String desc) {
    return Arrays.asList(values()).stream().filter(val -> StringUtils.equals(desc, val.name()))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("No location type corresonding to " + desc));
  }

  public boolean isUseAffiliate() {
    return this.equals(AFFILIATE);
  }

  public boolean isUseCustomerNr() {
    return this.equals(CUSTOMER);
  }
}
