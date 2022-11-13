package com.sagag.services.stakis.erp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.sagag.services.common.profiles.CzProfile;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.webservice.stakis-erp")
@CzProfile
public class StakisErpProperties {

  private String cisContextPath;

  private String cisSvc;

  private String tmConnectContextPath;

  private String tmConnectSvc;

  private StakisConfigData config;
}
