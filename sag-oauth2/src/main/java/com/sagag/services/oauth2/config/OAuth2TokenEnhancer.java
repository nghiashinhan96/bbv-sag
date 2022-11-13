package com.sagag.services.oauth2.config;

import com.sagag.services.oauth2.model.user.EshopUserDetails;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom token enhancer class.
 */
public class OAuth2TokenEnhancer implements TokenEnhancer {

  private static final String ADDITIONAL_INFO_SALE_ID = "sales_onbehalf";

  private static final String LOCATED_AFFILIATE_KEY = "located_affiliate";

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
      OAuth2Authentication authentication) {
    final EshopUserDetails user = (EshopUserDetails) authentication.getPrincipal();
    final Map<String, Object> additionalInfo = new HashMap<>();
    additionalInfo.put(ADDITIONAL_INFO_SALE_ID, user.getSalesId());
    additionalInfo.put(LOCATED_AFFILIATE_KEY, user.getLocatedAffiliate());
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    // remove refresh token out of the application response
    ((DefaultOAuth2AccessToken) accessToken).setRefreshToken(null);
    return accessToken;
  }
}
