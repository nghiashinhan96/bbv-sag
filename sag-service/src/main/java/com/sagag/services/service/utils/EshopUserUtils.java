package com.sagag.services.service.utils;

import com.sagag.services.common.contants.EshopUserConstants;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

@UtilityClass
public class EshopUserUtils {

  public static String buildRedirectUrl(String redirectUrl, String code, String hashUsernameCode) {
    return StringUtils.join(redirectUrl, "?code=", code, "&reg=", hashUsernameCode);
  }

  public static String buildOnbehalfUsername(String custNr) {
    Assert.hasText(custNr, "The given custNr must not be empty");
    return EshopUserConstants.ON_BEHALF_AGENT_PREFIX + custNr;
  }

}
