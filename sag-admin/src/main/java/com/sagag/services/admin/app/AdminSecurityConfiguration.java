package com.sagag.services.admin.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminSecurityConfiguration extends GlobalMethodSecurityConfiguration {

  @Autowired
  private AdminCorsFilter adminCorsFilter;

  /**
   * Initializes the CORS filter.
   *
   * @return the {@link FilterRegistrationBean} instance.
   */
  @Bean
  public FilterRegistrationBean<AdminCorsFilter> corsFilter() {
    final FilterRegistrationBean<AdminCorsFilter> bean = new FilterRegistrationBean<>();
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    bean.setFilter(adminCorsFilter);
    return bean;
  }

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    return new OAuth2MethodSecurityExpressionHandler();
  }

}
