package com.sagag.services.oauth2.config;

import com.sagag.services.oauth2.exceptionhandler.OAuth2WebResponseExceptionTranslator;
import com.sagag.services.oauth2.provider.EshopOAuth2RequestFactory;
import com.sagag.services.oauth2.security.CustomTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  private final AuthenticationManager authenticationManager;

  private final ClientDetailsService clientDetailService;

  private final TokenStore cachedTokenStore;

  @Autowired
  public AuthorizationServerConfiguration(
    @Qualifier("authenticationManagerBean") final AuthenticationManager authenticationManager,
    @Qualifier("clientDetailsServiceImpl") final ClientDetailsService clientDetailService,
    @Qualifier("tokenStore") final TokenStore cachedTokenStore) {
    this.authenticationManager = authenticationManager;
    this.clientDetailService = clientDetailService;
    this.cachedTokenStore = cachedTokenStore;
  }

  @Bean
  public JwtAccessTokenConverter tokenConverter() {
    // with SSL, using sign key to authorize the request
    return new JwtAccessTokenConverter();
  }

  @Bean
  public TokenEnhancer tokenEnhancer() {
    return new OAuth2TokenEnhancer();
  }

  @Bean
  public TokenEnhancerChain tokenEnhancerChain() {
    final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), tokenConverter()));
    return tokenEnhancerChain;
  }

  @Override
  public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints.tokenServices(defaultTokenServices())
        .authenticationManager(this.authenticationManager)
        .requestFactory(oauth2RequestFactory())
        .exceptionTranslator(new OAuth2WebResponseExceptionTranslator());
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) {
    security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(clientDetailService);
  }

  @Bean
  @Primary
  public AuthorizationServerTokenServices defaultTokenServices() {
    final CustomTokenServices tokenServices = new CustomTokenServices();
    tokenServices.setSupportRefreshToken(true);
    tokenServices.setTokenStore(cachedTokenStore);
    tokenServices.setTokenEnhancer(tokenEnhancerChain());
    tokenServices.setAuthenticationManager(authenticationManager);
    return tokenServices;
  }

  @Bean
  public OAuth2RequestFactory oauth2RequestFactory() {
    return new EshopOAuth2RequestFactory(clientDetailService);
  }
}
