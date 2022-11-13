package com.sagag.services.common.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.Optional;

@Configuration
@ConfigurationProperties("proxy")
@Data
@Slf4j
public class ProxyProperties {

  private String host;
  private Integer port;
  private String schema;

  private boolean hasProxyConfig() {
    return !StringUtils.isBlank(host)
        && Objects.nonNull(port)
        && !StringUtils.isBlank(schema);
  }

  public Optional<HttpHost> buildHttpHost() {
    if (!hasProxyConfig()) {
      return Optional.empty();
    }
    log.info("Proxy properties host = {} - port = {} - schema = {}", host, port, schema);
    return Optional.of(new HttpHost(host, port, schema));
  }

}
