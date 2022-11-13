package com.sagag.services.incentive.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "incentive")
@Data
public class IncentiveProperties {

  private IncentivePointProperties happyPoints;

  private IncentivePointProperties happyBonus;

  private IncentivePointProperties bigPoints;

  private IncentivePointProperties milesPoints;

  private IncentivePointProperties atHappyPoints;
  
  private IncentivePointProperties chHappyPoints;

  private IncentiveEndpointInfo technoOutlet;

  private IncentiveEndpointInfo ddchOutlet;

  private TrainingConfiguration training;
}
