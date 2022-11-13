package com.sagag.services.rest.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * Security configuration class for RESTfull application.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class RestSecurityConfiguration extends GlobalMethodSecurityConfiguration {

  @Autowired
  private PermissionEvaluator permissionEvaluator;

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private RestCorsFilter restCorsFilter;

  /**
   * Initializes the CORS filter.
   *
   * @return the {@link FilterRegistrationBean} instance.
   */
  @Bean
  public FilterRegistrationBean<RestCorsFilter> corsFilter() {
    final FilterRegistrationBean<RestCorsFilter> bean = new FilterRegistrationBean<>();
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    bean.setFilter(restCorsFilter);
    return bean;
  }

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    OAuth2MethodSecurityExpressionHandler handler = new OAuth2MethodSecurityExpressionHandler();
    handler.setPermissionEvaluator(permissionEvaluator);
    handler.setApplicationContext(applicationContext);
    return handler;
  }
}
