package com.sagag.services.incentive.linkgenerator.impl;

import com.sagag.services.incentive.authcookie.AuthCookieFactory;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.config.IncentivePointProperties;
import com.sagag.services.incentive.config.IncentiveProperties;
import com.sagag.services.incentive.domain.IncentiveLoginDto;
import com.sagag.services.incentive.domain.IncentivePasswordHashDto;
import com.sagag.services.incentive.linkgenerator.IncentiveLinkGenerator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.function.Supplier;

@Slf4j
public abstract class HappyBonusLinkGeneratorImpl implements IncentiveLinkGenerator {

  private static final int REQUIRED_SUPPLIERS = 3;

  @Autowired
  protected IncentiveProperties incentiveProps;

  @Override
  public String generate(IncentiveLoginDto login) throws CookiePrivacyException {
    log.debug("Return Happy Bonus link with = {} - properties = {}", login, incentiveProps.getHappyBonus());
    IncentivePointProperties properties = incentiveProps.getHappyBonus();
    final String loginToken = AuthCookieFactory.createLoginToken(login,
        properties.getAccessPointKey(), properties.getSessionTimeoutMs());
    final String url = String.format(properties.getUrl(), loginToken);
    log.debug("Happy Bonus Link generated: {}", url);
    return url;
  }

  @Override
  public IncentiveLoginDto buildLogin(Supplier<?>... suppliers) {
    Assert.notEmpty(suppliers, "The given incentive login request must not be null");
    Assert.isTrue(suppliers.length == REQUIRED_SUPPLIERS,
        "The given suppliers must match with required number");

    final IncentiveLoginDto login = new IncentiveLoginDto();
    login.setUserName((String) suppliers[1].get());
    IncentivePasswordHashDto passwordHash = (IncentivePasswordHashDto) suppliers[2].get();
    login.setPassword(passwordHash.getPassword());
    login.setType(passwordHash.getHashType());
    return login;
  }

}
