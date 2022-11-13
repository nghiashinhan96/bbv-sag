package com.sagag.services.dvse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.sagag.services.dvse.domain.WebSocketConfigInfo;
import com.sagag.services.dvse.domain.WebserviceConfigInfo;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "soap")
public class SoapProperties {

  private String urlMapping;

  private WebserviceConfigInfo dvse;

  private WebserviceConfigInfo tmconnect;

  private WebserviceConfigInfo unicat;

  private WebserviceConfigInfo facade;

  private WebserviceConfigInfo facade1;

  private WebSocketConfigInfo webSocket;
}
