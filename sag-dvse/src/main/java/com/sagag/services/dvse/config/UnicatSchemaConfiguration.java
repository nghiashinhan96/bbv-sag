package com.sagag.services.dvse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;

@Configuration
public class UnicatSchemaConfiguration {

  @Autowired
  private SoapProperties soapProps;

  @Bean(name = "unicat_catalog")
  public SimpleWsdl11Definition simpleWsdl11Definition() {
    return new SimpleWsdl11Definition(new ClassPathResource(soapProps.getUnicat().getWsdlName()));
  }

}
