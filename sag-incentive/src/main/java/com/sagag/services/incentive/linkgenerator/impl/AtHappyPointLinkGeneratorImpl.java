package com.sagag.services.incentive.linkgenerator.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.config.IncentiveProfile;
import com.sagag.services.incentive.config.IncentiveProperties;
import com.sagag.services.incentive.domain.IncentiveLoginDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@IncentiveProfile
public class AtHappyPointLinkGeneratorImpl extends BigPointLinkGeneratorImpl {

  @Autowired
  private IncentiveProperties incentiveProps;

  @Override
  public IncentiveMode support(SupportedAffiliate affiliate, boolean showHappyPoints) {
    if (showHappyPoints && affiliate != null && affiliate.isAtAffiliate()) {
      return IncentiveMode.AT_HAPPY_POINTS;
    }
    return null;
  }

  @Override
  public String generate(IncentiveLoginDto login) throws CookiePrivacyException {
    return generate(login, incentiveProps.getAtHappyPoints());
  }
}
