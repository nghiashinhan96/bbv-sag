package com.sagag.services.mdm.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.webservice.dvse")
public class MdmAccountConfigData {

  private String uri;

  private String mdmUsername;

  private String mdmPassword;

  private Map<String, String> templateCustomerIdMap;

  private String catalogUri;

  private Map<String, String> catalogIdMap;

}
