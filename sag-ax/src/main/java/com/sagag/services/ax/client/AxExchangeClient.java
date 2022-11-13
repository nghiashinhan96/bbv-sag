package com.sagag.services.ax.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.profiles.AxProfile;

@Component
@AxProfile
public class AxExchangeClient {

  @Autowired
  @Qualifier("axRestTemplate")
  private RestTemplate restTemplate;

  @LogExecutionTime(value = "Perf:AxExchangeClient -> exchange -> Execute AX API in {} ms",
      infoMode = true)
  public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
    HttpEntity<?> httpEntity, Class<T> responseClazz) {
    return restTemplate.exchange(url, method, httpEntity, responseClazz);
  }

  @LogExecutionTime(value = "Perf:AxExchangeClient -> exchangeByURI -> Execute AX API in {} ms",
      infoMode = true)
  public <T> ResponseEntity<T> exchangeByURI(URI url, HttpMethod method,
    HttpEntity<?> httpEntity, Class<T> responseClazz) {
    return restTemplate.exchange(url, method, httpEntity, responseClazz);
  }

}
