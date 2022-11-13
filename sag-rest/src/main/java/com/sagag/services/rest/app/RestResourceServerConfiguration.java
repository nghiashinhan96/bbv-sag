package com.sagag.services.rest.app;

import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.rest.authorization.RestPermissionEvaluator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Rest Resource server configuration class.
 */
@Configuration
@EnableResourceServer
public class RestResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  public static final String RESOURCE_ID = "sag-rest";

  private static final String[] MODIFY_USER_CONFIGURATION_AUTHORITY_GROUP = new String[] {
      EshopAuthority.SYSTEM_ADMIN.name(),
      EshopAuthority.GROUP_ADMIN.name(),
      EshopAuthority.USER_ADMIN.name(),
      EshopAuthority.FINAL_USER_ADMIN.name(),
      EshopAuthority.AUTONET_USER_ADMIN.name()
  };

  private static final String[] CONNECT_FEATURES_SUPPORT_FOR_SALES_AUTHORITY_GROUP = new String[] {
      EshopAuthority.SALES_ASSISTANT.name(),
      EshopAuthority.NORMAL_USER.name(),
      EshopAuthority.USER_ADMIN.name(),
      EshopAuthority.FINAL_USER_ADMIN.name(),
      EshopAuthority.FINAL_NORMAL_USER.name()

  };

  private static final String[] CONNECT_FEATURES_AUTHORITY_GROUP = new String[] {
      EshopAuthority.NORMAL_USER.name(),
      EshopAuthority.USER_ADMIN.name(),
      EshopAuthority.FINAL_USER_ADMIN.name(),
      EshopAuthority.FINAL_NORMAL_USER.name(),
      EshopAuthority.AUTONET_USER_ADMIN.name()
  };

  @Autowired
  private RestPermissionEvaluator permissionEvaluator;

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
    resources.resourceId(RESOURCE_ID).tokenServices(tokenService());
  }

  @Override
  public void configure(final HttpSecurity http) throws Exception {
    // @formatter:off
    http.authorizeRequests()
        .antMatchers(
            "/",
            "/release",
            "/dms/download",
            "/public/**",
            "/webjars/**",
            "/settings/**",
            "/user/send-code",
            "/user/forgot-password/code",
            "/customers/info",
            "/customers/register",
            "/affiliate/customers/register",
            "/haynespro/callback",
            "/user/forgot-password/reset-password",
            "/analytics/settings")
            .permitAll()
        .antMatchers(
            "/admin/affiliates/**")
            .hasAuthority(EshopAuthority.SYSTEM_ADMIN.name())
        .antMatchers(
            "/users/**",
            "/customer/users/**",
            "/customer/settings/**",
            "/admin/permissions/**")
            .hasAnyAuthority(MODIFY_USER_CONFIGURATION_AUTHORITY_GROUP)
        .antMatchers(
            "/marketing/clusters/*",
            "/marketing/coupons/*")
            .hasAuthority(EshopAuthority.MARKETING_ASSISTANT.name())
        .antMatchers(
            "/affiliate/customers/**")
            .hasAuthority(EshopAuthority.GROUP_ADMIN.name())
        .antMatchers(
            "/wss/opening-days/**")
            .hasAuthority(EshopAuthority.USER_ADMIN.name())
        .antMatchers(
            "/context/**",
            "/user/**")
            .authenticated()
        .antMatchers(
            "/analytics/**",
            "/order/sale/history",
            "/order/history/detail",
            "/customers/search",
            "/invoice/archives/**",
            "/return/**")
            .hasAnyAuthority(CONNECT_FEATURES_SUPPORT_FOR_SALES_AUTHORITY_GROUP)
        .antMatchers(
            "/dms/export/offer",
            "/cart/**",
            "/gtmotive/**",
            "/incentive/**",
            "/vin/**",
            "/order/**",
            "/part/**",
            "/search/**",
            "/categories/**",
            "/articles/**",
            "/history/**",
            "/coupons/**",
            "/customers/credit/**",
            "/autonet/**")
            .hasAnyAuthority(CONNECT_FEATURES_AUTHORITY_GROUP);
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
    tokenService.setAccessTokenConverter(tokenConverter());
    return tokenService;
  }

  /**
   * Constructs the access token converter which defines the algorithm to convert the token.
   *
   * @return the {@link JwtAccessTokenConverter}
   */
  @Bean
  public JwtAccessTokenConverter tokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setAccessTokenConverter(defaultAccessTokenConverter());
    return converter;
  }

  /**
   * Constructs the access token converter which defines the algorithm to convert the token.
   *
   * @return the {@link DefaultAccessTokenConverter}
   */
  @Bean
  public DefaultAccessTokenConverter defaultAccessTokenConverter() {
    final DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
    converter.setUserTokenConverter(customUserConverter());
    return converter;
  }

  /**
   * Constructs the backend Jwt token convert instance.
   *
   * @return the {@link BackendJwtTokenConverter}
   */
  @Bean
  public RestTokenConverter customUserConverter() {
    return new RestTokenConverter();
  }

  @Bean
  @Primary
  public PermissionEvaluator permissionEvaluator() {
    return permissionEvaluator;
  }

}
