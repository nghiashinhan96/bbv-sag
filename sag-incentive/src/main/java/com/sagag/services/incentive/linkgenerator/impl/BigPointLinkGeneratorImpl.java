package com.sagag.services.incentive.linkgenerator.impl;

import com.sagag.services.incentive.authcookie.CookieCrypt;
import com.sagag.services.incentive.config.IncentivePointProperties;
import com.sagag.services.incentive.domain.IncentiveLoginDto;
import com.sagag.services.incentive.linkgenerator.IncentiveLinkGenerator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.Assert;

import java.util.function.Supplier;

@Slf4j
public abstract class BigPointLinkGeneratorImpl implements IncentiveLinkGenerator {

  protected String generate(IncentiveLoginDto login, IncentivePointProperties properties) {
    log.debug("Return Big Points link with = {} - properties = {}", login, properties);
    // Note "secure" = sha1({shop_secure_key}+{project_id}+{shop_secure_key}+{foreign_user_id})
    String secure = CookieCrypt.createHashKey(properties.getHashEncrypt(), new StringBuilder()
        .append(properties.getSecureKey()).append(properties.getShopValue())
        .append(properties.getSecureKey()).append(login.getCustNr()).toString());
    return String.format(properties.getUrl(), login.getCustNr(), secure);
  }

  @Override
  public IncentiveLoginDto buildLogin(Supplier<?>... suppliers) {
    Assert.notEmpty(suppliers, "The given incentive login request must not be null");
    final IncentiveLoginDto login = new IncentiveLoginDto();
    login.setCustNr(getCustomerNr(suppliers[0]));
    return login;
  }

}
