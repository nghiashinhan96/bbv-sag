package com.sagag.services.oates.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.transport.http.HttpTransportConstants;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.oates.config.OatesProfile;

import lombok.extern.slf4j.Slf4j;

@Component
@OatesProfile
@Slf4j
public class OatesExchangeClient {

  @Autowired
  @Qualifier("oatesRestTemplate")
  private RestTemplate restTemplate;

  @LogExecutionTime(value = "Perf:OatesExchangeClient -> exchange -> Execute Oates API in {} ms",
      infoMode = true)
  public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
      Class<T> responseClazz) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    httpHeaders.add(HttpHeaders.ACCEPT_ENCODING, HttpTransportConstants.CONTENT_ENCODING_GZIP);

    log.info("Oates request: URL = {}\nHeaders = {} ", url, httpHeaders);
    final ResponseEntity<T> responseEntity =
        restTemplate.exchange(url, method, new HttpEntity<>(httpHeaders), responseClazz);
    log.info("Body Response: \n{}",
        SagJSONUtil.convertObjectToPrettyJson(responseEntity.getBody()));
    return responseEntity;
  }
}
