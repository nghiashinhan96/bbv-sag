package com.sagag.services.common.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

/**
 * The Proxy configuration class.
 */
@Configuration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class HttpProxyConfiguration {

  @Value("${sag.rest.maxConnPerRoute}")
  private Integer maxConnPerRoute;

  @Value("${sag.rest.maxConnTotal}")
  private Integer maxConnTotal;

  @Value("${sag.rest.connection-request-timeout}")
  private Integer connectionRequestTimeout;

  @Value("${sag.rest.connect-timeout}")
  private Integer connectionTimeout;

  @Value("${sag.rest.read-timeout}")
  private Integer readTimeout;

  @Autowired
  private ProxyProperties proxyProps;

  @Bean
  public HttpClient httpClient() {
    final HttpClientBuilder builder = HttpClientBuilder.create();
    builder.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor());
    builder.setMaxConnPerRoute(maxConnPerRoute);
    builder.setMaxConnTotal(maxConnTotal);
    proxyProps.buildHttpHost().ifPresent(builder::setProxy);
    return builder.build();
  }

  @Bean
  public ClientHttpRequestFactory httpRequestFactory() {
    return new HttpComponentsClientHttpRequestFactory(httpClient());
  }

  @Bean
  public HttpComponentsMessageSender httpComponentsMessageSender() {
    return new HttpComponentsMessageSender(httpClient());
  }

  public int getConnectionRequestTimeout() {
    return this.connectionRequestTimeout;
  }

  public int getConnectionTimeout() {
    return this.connectionTimeout;
  }

  public int getReadTimeout() {
    return this.readTimeout;
  }

  public int getMaxConnectionPerRoute() {
    return this.maxConnPerRoute;
  }

  public int getMaxConnectionTotal() {
    return this.maxConnTotal;
  }

}
