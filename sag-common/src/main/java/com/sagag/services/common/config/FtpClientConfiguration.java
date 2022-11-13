package com.sagag.services.common.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("external.webservice.ftp")
@Data
public class FtpClientConfiguration {

  private boolean enabled;
  private String view;
  private String upload;
  private String user;
  private String pass;
  private int port = 21;
  private int timeout;
  private String parentDir;

  public boolean hasFtpConfig() {
    return enabled;
  }

}
