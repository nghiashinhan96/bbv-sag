package com.sagag.services.admin.app;

import com.sagag.services.domain.eshop.common.EshopAuthority;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
public class AdminResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  public static final String ADMIN_RESOURCE_ID = "sag-admin";

  @Value("${sag.security.auth.host}")
  private String authHost;

  @Value("${sag.security.auth.clientId}")
  private String authClientId;

  @Value("${sag.security.auth.clientSecret}")
  private String authClientSecret;

  @Value("${sag.security.auth.checkTokenUrl}")
  private String authCheckTokenUrl;

  @Override
  public void configure(final ResourceServerSecurityConfigurer resources) {
    resources.resourceId(ADMIN_RESOURCE_ID).tokenServices(tokenService());
  }

  @Override
  public void configure(final HttpSecurity http) throws Exception {
    // @formatter:off
    http.authorizeRequests()
        .antMatchers(
            "/admin/users/system-admin/password/reset")
            .permitAll()
        .antMatchers(
            "/admin/**")
            .hasAuthority(EshopAuthority.SYSTEM_ADMIN.name());
  } // @formatter:on

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
