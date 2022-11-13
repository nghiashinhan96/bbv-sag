package com.sagag.services.oauth2.provider;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

import java.util.Map;

@Slf4j
public class EshopOAuth2RequestFactory extends DefaultOAuth2RequestFactory {

  public EshopOAuth2RequestFactory(ClientDetailsService clientDetailsService) {
    super(clientDetailsService);
  }

  @Override
  public TokenRequest createTokenRequest(Map<String, String> requestParameters,
      ClientDetails authenticatedClient) {
    log.debug("Creating token request = {} - client details = {}", requestParameters, authenticatedClient);
    return super.createTokenRequest(requestParameters, authenticatedClient);
  }
}
