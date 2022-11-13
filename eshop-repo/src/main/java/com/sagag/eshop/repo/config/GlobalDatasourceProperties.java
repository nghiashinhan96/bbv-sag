package com.sagag.eshop.repo.config;

import com.zaxxer.hikari.HikariConfig;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Global datasource properties configuration class.
 */
@Component
@ConfigurationProperties("spring.datasource")
@Data
public class GlobalDatasourceProperties {

  private String type;
  private HikariConfig hikari;
}
