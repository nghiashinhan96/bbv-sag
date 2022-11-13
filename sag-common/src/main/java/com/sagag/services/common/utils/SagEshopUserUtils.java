package com.sagag.services.common.utils;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class SagEshopUserUtils {

  private static final String NO_CUSTOMER_NAME = "No Customer Name";

  private static final int MAX_LENGTH_OF_VIRTUAL_USERNAME = 32;

  public String getDefaultVirtualUsername(String customerName) {
    final String customerNameStr = StringUtils.defaultIfBlank(customerName, NO_CUSTOMER_NAME);
    final String currentTimeStr = String.valueOf(System.currentTimeMillis());
    final String username = StringUtils.join(customerNameStr, currentTimeStr);

    final int maxLengthOfVirtualUsername = MAX_LENGTH_OF_VIRTUAL_USERNAME;
    if (StringUtils.length(username) <= maxLengthOfVirtualUsername) {
      return username;
    }
    return StringUtils.join(StringUtils.substring(customerNameStr, 0,
        maxLengthOfVirtualUsername - currentTimeStr.length()),
        currentTimeStr);
  }

  public String defaultHaynesProUsername(String username) {
    if (StringUtils.isBlank(username)) {
      return StringUtils.EMPTY;
    }
    if (StringUtils.length(username) <= MAX_LENGTH_OF_VIRTUAL_USERNAME) {
      return username;
    }
    return StringUtils.substring(username, 0, MAX_LENGTH_OF_VIRTUAL_USERNAME);
  }

}
