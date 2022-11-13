package com.sagag.services.rest.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * Main configuration class for Rest application.
 */
@Configuration
public class RestCoreConfiguration {

  @Bean
  @Autowired
  public MappedInterceptor getMappedInterceptor(
      VersionHandlerInterceptor versionHandlerInterceptor) {
      return new MappedInterceptor(new String[] { "/**" }, versionHandlerInterceptor);
  }

}
