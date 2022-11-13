package com.sagag.services.service.config;

import com.sagag.services.common.config.HttpProxyConfiguration;
import com.sagag.services.common.profiles.AxCzProfile;
import com.sagag.services.service.cart.support.ShopTypeDefaultHandlerMethodArgumentResolver;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

@Configuration
@PropertySource(value = {
    "classpath:i8n/messages.properties",
    "classpath:i8n/messages_de.properties",
    "classpath:i8n/messages_fr.properties",
    "classpath:i8n/messages_it.properties",
    "classpath:i8n/messages_cs.properties",
    "classpath:i8n/messages_en.properties",
    "classpath:i8n/messages_sr.properties",
    "classpath:license_vin.properties",
    "classpath:invoice_archive.properties",
    "classpath:SdokCertificate-5.jks",
    "classpath:erp_config.properties"
    })
@Slf4j
public class SagServiceCoreConfiguration implements WebMvcConfigurer {

  @Autowired
  private HttpProxyConfiguration httpProxyConfig;

  /**
   * Message externalization/internationalization.
   */
  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("i8n/messages");
    return messageSource;
  }

  @Bean
  public ShopTypeDefaultHandlerMethodArgumentResolver shopTypeDefaultResolver() {
    return new ShopTypeDefaultHandlerMethodArgumentResolver();
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(shopTypeDefaultResolver());
  }

  @Bean(name = "sdokHttpClient")
  @AxCzProfile
  public CloseableHttpClient sdokHttpClient() {
    final HttpClientBuilder builder = HttpClientBuilder.create();
    builder.setMaxConnPerRoute(httpProxyConfig.getMaxConnectionPerRoute());
    builder.setMaxConnTotal(httpProxyConfig.getMaxConnectionTotal());
    builder.setConnectionTimeToLive(httpProxyConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS);
    builder.setConnectionManager(sdokHttpClientConnManager());
    builder.setRetryHandler(new StandardHttpRequestRetryHandler());
    return builder.build();
  }

  @Bean
  @AxCzProfile
  public PoolingHttpClientConnectionManager sdokHttpClientConnManager() {
    final SSLConnectionSocketFactory factory =
        new SSLConnectionSocketFactory(sdokSSLContext(), NoopHostnameVerifier.INSTANCE);
    final Registry<ConnectionSocketFactory> registry =
        RegistryBuilder.<ConnectionSocketFactory>create().register("https", factory).build();
    final PoolingHttpClientConnectionManager connManager =
        new PoolingHttpClientConnectionManager(registry);
    connManager.setDefaultMaxPerRoute(httpProxyConfig.getMaxConnectionPerRoute());

    final SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
    connManager.setDefaultSocketConfig(socketConfig);
    connManager.setValidateAfterInactivity(1000);
    return connManager;
  }

  @Bean
  @AxCzProfile
  public SSLContext sdokSSLContext() {
    SSLContext sslContext = null;
    File file;
    try {
      file = new ClassPathResource("SdokCertificate-5.jks").getFile();
      log.info("Absolute certificate file path: {}", file.getAbsolutePath());
      sslContext = new SSLContextBuilder().loadTrustMaterial(file).build();
      log.debug("SDOK SSLContext is created");
      return sslContext;
    } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException
        | CertificateException e) {
      log.error("Error while loading SDOK certificate truststore file", e);
      throw new IllegalArgumentException(e);
    }

  }
}
