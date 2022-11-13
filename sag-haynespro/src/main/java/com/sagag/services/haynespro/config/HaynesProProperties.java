package com.sagag.services.haynespro.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@HaynesProProfile
@ConfigurationProperties(prefix = "external.webservice.haynespro")
public class HaynesProProperties {

  private String uri;

  private String wsdl;

  private String companyIdentificaton;

  private String distributorPassword;

  private String callbackApi;

}
