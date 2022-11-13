package com.sagag.services.oates.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@OatesProfile
public class OatesConfiguration {

  @Autowired
  private ClientHttpRequestFactory httpRequestFactory;

  @Bean(name = "oatesRestTemplate")
  @Lazy
  public RestTemplate restTemplate() {
    return new RestTemplate(httpRequestFactory);
  }

}
