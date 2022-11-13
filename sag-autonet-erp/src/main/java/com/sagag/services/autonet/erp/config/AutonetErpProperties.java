package com.sagag.services.autonet.erp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.webservice.autonet-erp")
public class AutonetErpProperties {

  private String wsdl;

  private String contextPath;

  private String soapActionGetErpInformation;

}
