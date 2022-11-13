package com.sagag.services.incentive.linkgenerator.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.authcookie.CookieCrypt;
import com.sagag.services.incentive.config.IncentivePointProperties;
import com.sagag.services.incentive.config.IncentiveProfile;
import com.sagag.services.incentive.config.IncentiveProperties;
import com.sagag.services.incentive.domain.IncentiveLoginDto;
import com.sagag.services.incentive.linkgenerator.IncentiveLinkGenerator;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.function.Supplier;

@Component
@IncentiveProfile
@Slf4j
public class MilesLinkGeneratorImpl implements IncentiveLinkGenerator {

  @Autowired
  private IncentiveProperties incentiveProps;

  @Override
  public String generate(IncentiveLoginDto login) {
    log.debug("Return Miles link with = {} - properties = {}", login, incentiveProps.getMilesPoints());
    IncentivePointProperties properties = incentiveProps.getMilesPoints();
    String hashKeyValue = CookieCrypt.createHashKey(properties.getHashEncrypt(), new StringBuilder()
        .append(properties.getSecureKey()).append(properties.getShopValue())
        .append(login.getCustNr()).toString());
    if (StringUtils.isBlank(hashKeyValue)) {
      return StringUtils.EMPTY;
    }
    return String.format(properties.getUrl(), login.getCustNr(), hashKeyValue);
  }

  @Override
  public IncentiveMode support(SupportedAffiliate affiliate, boolean showHappyPoints) {
    if (showHappyPoints && affiliate != null && affiliate.isTnm()) {
      return IncentiveMode.MILES;
    }
    return null;
  }

  @Override
  public IncentiveLoginDto buildLogin(Supplier<?>... suppliers) {
    Assert.notEmpty(suppliers, "The given incentive login request must not be null");
    final IncentiveLoginDto login = new IncentiveLoginDto();
    login.setCustNr(getCustomerNr(suppliers[0]));
    return login;
  }

}
