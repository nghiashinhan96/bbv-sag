package com.sagag.services.hazelcast.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

/**
 * HttpClient configuration class for integration testing purpose.
 */
@Configuration
@Profile("test")
public class HttpClientConfiguration {

  @Bean
  public HttpClient httpClient() {
    final RequestConfig requestConfig = RequestConfig.custom().build();
    final HttpClientBuilder builder = HttpClientBuilder.create();
    builder.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor())
        .setDefaultRequestConfig(requestConfig);
    return builder.build();
  }
}
