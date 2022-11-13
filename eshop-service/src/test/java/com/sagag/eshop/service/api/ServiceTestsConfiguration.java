package com.sagag.eshop.service.api;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

/**
 * Tests configuration class.
 */
@Configuration
@Profile("test")
public class ServiceTestsConfiguration {

  /**
   * Initializes the HttpClient instance.
   * 
   * @return the {@link HttpClient}
   * @throws Exception thrown when program fails
   */
  @Bean
  public HttpClient httpClient() throws Exception {
    final HttpClientBuilder builder = HttpClientBuilder.create();
    builder.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor());
    return builder.build();
  }
}
