package com.sagag.services.tools.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import java.util.Objects;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

/**
 * Class for import proxy configuration for MDM.
 *
 */
@Configuration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class MdmProxyConfiguration {

  @Value("${proxy.host:}")
  private String proxyHost;

  @Value("${proxy.port:}")
  private Integer proxyPort;

  @Value("${proxy.schema:}")
  private String proxySchema;

  @Bean
  public MessageFactory messageFactory() throws SOAPException {
    return MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
  }

  @Bean
  public SaajSoapMessageFactory saajSoapMessageFactory() throws SOAPException {
    return new SaajSoapMessageFactory(messageFactory());
  }

  @Bean
  public ClientHttpRequestFactory httpRequestFactory() {
    return new HttpComponentsClientHttpRequestFactory(httpClient());
  }

  @Bean
  public HttpClient httpClient() {
    final HttpClientBuilder builder = HttpClientBuilder.create();
    builder.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor());
    if (hasProxyConfig()) {
      builder.setProxy(new HttpHost(proxyHost, proxyPort, proxySchema));
    }
    return builder.build();
  }

  private boolean hasProxyConfig() {
    return !StringUtils.isBlank(proxyHost) && Objects.nonNull(proxyPort) && !StringUtils.isBlank(proxySchema);
  }

  @Bean
  public HttpComponentsMessageSender httpComponentsMessageSender() {
    return new HttpComponentsMessageSender(httpClient());
  }

}
