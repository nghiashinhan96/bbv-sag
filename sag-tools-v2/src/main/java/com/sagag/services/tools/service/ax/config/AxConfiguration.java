package com.sagag.services.tools.service.ax.config;

import com.sagag.services.tools.service.SingleSignOnService;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * The configuration for Ax component.
 * </p>
 *
 */
@Configuration
public class AxConfiguration {

  @Value("${external.webservice.ax.tokenPath}")
  private String tokenPath;

  @Value("${external.webservice.ax.clientId}")
  private String clientId;

  @Value("${external.webservice.ax.userName}")
  private String userName;

  @Value("${external.webservice.ax.password}")
  private String password;

  @Autowired
  private SingleSignOnService ssoService;

  @Autowired
  private HttpClient httpClient;

  /**
   * Constructs the http request factory instance.
   *
   * @return the {@link HttpComponentsClientHttpRequestFactory}
   * @throws Exception thrown when the bean construction fails
   */
  @Bean
  @ConfigurationProperties(prefix = "sag.rest.erp.timeout")
  public ClientHttpRequestFactory httpRequestFactory() {
    return new HttpComponentsClientHttpRequestFactory(httpClient);
  }

  @Bean(name = "axRestTemplate")
  @Lazy
  public RestTemplate restTemplate() {
    return new RestTemplate(httpRequestFactory());
  }

  @Bean(name = "axInitialResource", autowire = Autowire.BY_NAME)
  public AxInitialResource axInitialResource() {
    final AxInitialResource resource = new AxInitialResource();
    resource.setAccessToken(ssoService.authorize(tokenPath, clientId, userName, password));
    return resource;
  }

}
