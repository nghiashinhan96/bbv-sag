package com.sagag.services.gtmotive.app;

import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.request.AuthenticationData;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("external.webservice.vehicle")
@GtmotiveProfile
@Data
public class GtmotiveVehicleAccountConfiguration {

  private String url;
  private String gsId;
  private String password;
  private String customerId;
  private String userId;

  public AuthenticationData toAuthenticationData() {
    return AuthenticationData.builder().gsId(gsId).gsPwd(password).customerId(customerId)
        .userId(userId).build();
  }
}
