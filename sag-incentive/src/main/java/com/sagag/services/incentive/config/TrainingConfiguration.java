package com.sagag.services.incentive.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "traning")
public class TrainingConfiguration {

  private String companyPassword;
  private String hashEncrypt;
}
