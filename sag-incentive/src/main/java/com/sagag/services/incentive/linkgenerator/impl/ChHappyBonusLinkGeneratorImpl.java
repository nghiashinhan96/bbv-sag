package com.sagag.services.incentive.linkgenerator.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.client.HappyBonusClient;
import com.sagag.services.incentive.config.IncentivePointProperties;
import com.sagag.services.incentive.config.IncentiveProfile;
import com.sagag.services.incentive.domain.AccessTokenRequest;
import com.sagag.services.incentive.domain.IncentiveLoginDto;
import com.sagag.services.incentive.response.AccessTokenResponse;
import com.sagag.services.incentive.response.AccessUrlResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.function.Supplier;

@Component
@IncentiveProfile
@Slf4j
public class ChHappyBonusLinkGeneratorImpl extends HappyBonusLinkGeneratorImpl {

  private static final String GRANT_TYPE = "client_credentials";

  @Autowired
  private HappyBonusClient happyBonusClient;

  @Override
  public String generate(IncentiveLoginDto login) throws CookiePrivacyException {
    log.debug("Return Happy Bonus link with = {} - properties = {}", login,
        incentiveProps.getChHappyPoints());
    IncentivePointProperties properties = incentiveProps.getChHappyPoints();
    final String getTokenEndpoint = properties.getTokenUrl();
    final String getAccessUrlEndpoint = properties.getAuthUrl();
    final String clientId = properties.getShopValue();
    final String clientSecret = properties.getSecureKey();
    Assert.isTrue(StringUtils.isNotBlank(getTokenEndpoint),
        "Get token Endpoint for happy bonus is not yet configured");
    Assert.isTrue(StringUtils.isNotBlank(getAccessUrlEndpoint),
        "Get access url endpoint for happy bonus is not yet configured");
    Assert.isTrue(StringUtils.isNotBlank(clientId),
        "Client id for happy bonus is not yet configured");
    Assert.isTrue(StringUtils.isNotBlank(clientSecret),
        "Client secret for happy bonus is not yet configured");

    AccessTokenRequest accessTokenRequestBody = AccessTokenRequest.builder().clientId(clientId)
        .clientSecret(clientSecret).grantType(GRANT_TYPE).build();
    AccessTokenResponse accessTokenResponse =
        happyBonusClient.getAccessToken(getTokenEndpoint, accessTokenRequestBody);
    if (!isValidTokenResponse(accessTokenResponse)) {
      return null;
    }

    String accessToken = accessTokenResponse.getAccessToken();
    final String formatAccessUrlEndpoint = String.format(getAccessUrlEndpoint, login.getCustNr());

    AccessUrlResponse accessUrlResponse =
        happyBonusClient.getAccessUrl(formatAccessUrlEndpoint, accessToken);

    String url = null;
    if (accessUrlResponse != null) {
      url = accessUrlResponse.getRedirectUrlFragment();
    }
    log.debug("Happy Bonus Link generated: {}", url);
    return url;
  }

  @Override
  public IncentiveMode support(SupportedAffiliate affiliate, boolean showHappyPoints) {
    if (showHappyPoints && affiliate != null && affiliate.isDch()) {
      return IncentiveMode.CH_HAPPY_POINTS;
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

  private boolean isValidTokenResponse(AccessTokenResponse accessTokenResponse) {
    return accessTokenResponse != null
        && StringUtils.isNotBlank(accessTokenResponse.getAccessToken());
  }

}
