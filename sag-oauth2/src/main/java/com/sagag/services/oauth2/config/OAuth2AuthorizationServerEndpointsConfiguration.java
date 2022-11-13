package com.sagag.services.oauth2.config;

import com.sagag.services.oauth2.authenticator.OAuth2UserAuthenticator;
import com.sagag.services.oauth2.endpoint.ExternalUserVisitEndpoint;
import com.sagag.services.oauth2.model.VisitRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

@Configuration
public class OAuth2AuthorizationServerEndpointsConfiguration {

  private final OAuth2UserAuthenticator<VisitRegistration> userAuthenticator;

  @Autowired
  public OAuth2AuthorizationServerEndpointsConfiguration(
    final OAuth2UserAuthenticator<VisitRegistration> userAuthenticator) {
    this.userAuthenticator = userAuthenticator;
  }

  @Bean
  public ExternalUserVisitEndpoint externalUserVisitEndpoint() {
    ExternalUserVisitEndpoint endpoint = new ExternalUserVisitEndpoint();
    endpoint.setUserAuthenticator(userAuthenticator);
    return endpoint;
  }

  @Bean
  @ConditionalOnMissingBean(OAuth2UserAuthenticator.class)
  public OAuth2UserAuthenticator<VisitRegistration> emptyUserAuthenticator() {
    return new OAuth2UserAuthenticator<VisitRegistration>() {

      @Override
      public OAuth2AccessToken authenticate(VisitRegistration session, String... args) {
        throw new UnsupportedOperationException();
      }

    };
  }
}
