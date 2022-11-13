package com.sagag.services.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;

@Configuration
public class OAuth2Configuration {

  public static final String RESOURCE_ID = "sag-rest";
  public static final String ADMIN_RESOURCE_ID = "sag-admin";

  @Bean
  public MBeanExporter exporter() {
    final MBeanExporter exporter = new AnnotationMBeanExporter();
    exporter.setAutodetect(true);
    exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
    exporter.setExcludedBeans("dataSource");
    return exporter;
  }

}
