package com.sagag.services.hazelcast.app;

import com.zaxxer.hikari.HikariConfig;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Global datasource properties configuration class.
 */
@Component
@ConfigurationProperties("spring.hz")
@Data
public class HazelcastDatasourceProperties {

  private String type;
  private HikariConfig hikari;
}
