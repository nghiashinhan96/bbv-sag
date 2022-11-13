package com.sagag.services.rest.app;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestDataAnalyticsConfiguration {

  @Autowired
  private HttpClient httpClient;

  /**
   * Constructs the http request factory instance.
   *
   * @return the {@link HttpComponentsClientHttpRequestFactory}
   * @throws Exception thrown when the bean construction fails
   */
  @Bean
  @ConfigurationProperties(prefix = "sag.rest.analytics.timeout")
  public ClientHttpRequestFactory httpRequestFactory() {
    return new HttpComponentsClientHttpRequestFactory(httpClient);
  }

  @Bean(name = "analyticsRestTemplate")
  public RestTemplate restTemplate() {
    return new RestTemplate(httpRequestFactory());
  }
}
