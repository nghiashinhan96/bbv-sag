package com.sagag.services.oauth2.authenticator.impl;

import com.sagag.services.oauth2.authenticator.OAuth2UserAuthenticator;
import com.sagag.services.oauth2.model.VisitRegistration;
import com.sagag.services.oauth2.profiles.OAuth2ExternalAuthenticatorMode;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

@Component
@OAuth2ExternalAuthenticatorMode
public class EmptyUserAuthenticator extends OAuth2UserAuthenticator<VisitRegistration> {

  @Override
  public OAuth2AccessToken authenticate(VisitRegistration session, String... args) {
    throw new UnsupportedOperationException("Unsupported authenticate external user as default");
  }

}
