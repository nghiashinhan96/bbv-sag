package com.sagag.services.incentive.builder.bonus;

import com.sagag.services.incentive.authcookie.AuthCookieFactory;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.builder.IncentiveUrlBuilder;
import com.sagag.services.incentive.config.IncentiveEndpointInfo;
import com.sagag.services.incentive.domain.IncentiveLoginDto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HappyBonusUrlBuilder implements IncentiveUrlBuilder {

  @NonNull
  private IncentiveEndpointInfo happyBonus;

  @NonNull
  private IncentiveLoginDto login;

  @Override
  public String buildUrl() throws CookiePrivacyException {
    final String loginToken = AuthCookieFactory.createLoginToken(login, happyBonus.getAccessPoint(),
        happyBonus.getSessionTimeout());
    return new StringBuilder(happyBonus.getUrl()).append("?token=").append(loginToken).toString();
  }

}
