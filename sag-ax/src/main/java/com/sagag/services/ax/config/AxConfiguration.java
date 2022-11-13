
package com.sagag.services.ax.config;

import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.sso.api.SingleSignOnService;
import java.util.NoSuchElementException;
import java.util.concurrent.RejectedExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

/**
 * <p>
 * The configuration for Ax component.
 * </p>
 *
 */
@Configuration
@Slf4j
@AxProfile
public class AxConfiguration {

  @Value("${external.webservice.ax.tokenPath}")
  private String tokenPath;

  @Value("${external.webservice.ax.clientId}")
  private String clientId;

  @Value("${external.webservice.ax.userName}")
  private String userName;

  @Value("${external.webservice.ax.password}")
  private String password;

  @Value("${sag.rest.maxConnPerRoute}")
  private Integer maxConnPerRoute;

  @Value("${sag.rest.maxConnTotal}")
  private Integer maxConnTotal;

  @Autowired
  private SingleSignOnService ssoService;

  @Bean
  public HttpClient axHttpClient() {
    final HttpClientBuilder builder = HttpClientBuilder.create();
    builder.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor());
    builder.setMaxConnPerRoute(maxConnPerRoute);
    builder.setMaxConnTotal(maxConnTotal);
    return builder.build();
  }

  /**
   * Constructs the http request factory instance.
   *
   * @return the {@link HttpComponentsClientHttpRequestFactory}
   * @throws Exception thrown when the bean construction fails
   */
  @Bean
  @ConfigurationProperties(prefix = "sag.rest.erp.timeout")
  public ClientHttpRequestFactory axHttpRequestFactory() {
    return new HttpComponentsClientHttpRequestFactory(axHttpClient());
  }

  @Bean(name = "axRestTemplate")
  @Lazy
  public RestTemplate restTemplate() {
    return new RestTemplate(axHttpRequestFactory());
  }

  @Bean(name = "axInitialResource", autowire = Autowire.BY_NAME)
  @DynamicAxProfile
  public AxInitialResource axInitialResource() {
    final AxInitialResource resource = new AxInitialResource();
    if (StringUtils.isEmpty(tokenPath)) {
      log.info("No need token for vpn erp type");
      return resource;
    }
    try {
      resource.setAccessToken(ssoService.authorize(tokenPath, clientId, userName, password));
      resource.setNeedRetry(false);
    } catch (NoSuchElementException | RejectedExecutionException ex) {
      // We will ignore exception when application startup
      resource.setNeedRetry(true);
      log.error("Get the ERP token has error", ex);
    }
    return resource;
  }

}
