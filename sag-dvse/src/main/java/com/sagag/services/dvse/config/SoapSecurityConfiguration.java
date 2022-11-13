package com.sagag.services.dvse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * Security configuration class for RESTfull application.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SoapSecurityConfiguration extends GlobalMethodSecurityConfiguration {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private SoapCorsFilter soapCorsFilter;

  /**
   * Initializes the CORS filter.
   *
   * @return the {@link FilterRegistrationBean} instance.
   */
  @Bean
  public FilterRegistrationBean<SoapCorsFilter> corsFilter() {
    final FilterRegistrationBean<SoapCorsFilter> bean = new FilterRegistrationBean<>();
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    bean.setFilter(soapCorsFilter);
    return bean;
  }

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    OAuth2MethodSecurityExpressionHandler handler = new OAuth2MethodSecurityExpressionHandler();
    handler.setApplicationContext(applicationContext);
    return handler;
  }

}
