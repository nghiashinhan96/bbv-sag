package com.sagag.services.copydb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import lombok.Data;

/**
 * Properties specific to Connect application.
 *
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "copy", ignoreUnknownFields = false)
@Data
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppProperties {

  private List<String> tables = new ArrayList<>();

  private final Artefacts artefacts = new Artefacts();

  private boolean compareIgnoreCase = Boolean.TRUE;

  private String fromSchema;

  private String toSchema;

  @Data
  public static final class Artefacts {

    private List<String> tables = new ArrayList<>();

  }

}
