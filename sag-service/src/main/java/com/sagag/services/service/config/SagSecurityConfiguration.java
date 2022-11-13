package com.sagag.services.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@Profile("test")
public class SagSecurityConfiguration {

  @Value("${sag.security.auth.host:}")
  private String authHost;

  @Value("${sag.security.auth.clientId:}")
  private String authClientId;

  @Value("${sag.security.auth.clientSecret:}")
  private String authClientSecret;

  @Value("${sag.security.auth.checkTokenUrl:}")
  private String authCheckTokenUrl;

  /**
   * Constructs the remote token services to verify the valid token in the request header.
   *
   * @return the {@link RemoteTokenServices}
   */
  @Primary
  @Bean
  public RemoteTokenServices tokenService() {
    final RemoteTokenServices tokenService = new RemoteTokenServices();
    tokenService.setCheckTokenEndpointUrl(authHost + authCheckTokenUrl);
    tokenService.setClientId(authClientId);
    tokenService.setClientSecret(authClientSecret);
    tokenService.setAccessTokenConverter(defaultAccessTokenConverter());
    return tokenService;
  }

  /**
   * Constructs the access token converter which defines the algorithm to convert the token.
   *
   * @return the {@link DefaultAccessTokenConverter}
   */
  @Bean
  public DefaultAccessTokenConverter defaultAccessTokenConverter() {
    return new DefaultAccessTokenConverter();
  }

}
