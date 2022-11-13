package com.sagag.services.dvse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;

import com.sagag.services.dvse.profiles.AxCzDvseProfile;
import com.sagag.services.dvse.profiles.DefaultDvseProfile;

@Configuration
public class DvseSchemaConfiguration {

  @Autowired
  private SoapProperties soapProps;

  @Bean(name = "catalog")
  @DefaultDvseProfile
  public SimpleWsdl11Definition catalogSimpleWsdl11Definition() {
    return new SimpleWsdl11Definition(new ClassPathResource(soapProps.getDvse().getWsdlName()));
  }

  @Bean(name = "tmconnect")
  @AxCzDvseProfile
  public SimpleWsdl11Definition tmconnectSimpleWsdl11Definition() {
    return new SimpleWsdl11Definition(new ClassPathResource(
        soapProps.getTmconnect().getWsdlName()));
  }
}
