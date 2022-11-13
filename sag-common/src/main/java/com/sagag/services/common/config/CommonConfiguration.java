package com.sagag.services.common.config;

import org.dozer.DozerBeanMapper;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CommonConfiguration {

  @Bean(name = "dozerBeanMapper")
  public DozerBeanMapper dozerBeanMapper() {
    return new DozerBeanMapper();
  }

  @Bean(name = "emailValidator")
  @Primary
  public EmailValidator emailValidator() {
    return new EmailValidator();
  }
}
