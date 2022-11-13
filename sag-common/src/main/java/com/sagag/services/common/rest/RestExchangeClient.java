package com.sagag.services.common.rest;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@Slf4j
public class RestExchangeClient {

  private static final String LOG_INFO_ERP_URL = "ERP request URL: \n{}";

  private static final String LOG_DEBUG_HEADER_REQUEST = "Headers request: \n{}";

  private static final String LOG_INFO_BODY_REQUEST = "Body request: \n{}";

  private static final String LOG_INFO_BODY_RESPONSE = "Body Response: \n{}";

  @LogExecutionTime(value = "Perf:RestExchangeClient -> exchange -> Execute RESTful API in {} ms",
      infoMode = true)
  public <T> ResponseEntity<T> exchange(final RestTemplate restTemplate,
      final String url, final HttpMethod method,
      final HttpEntity<?> httpEntity, final Class<T> responseClazz) {

    log.info(LOG_INFO_ERP_URL, url);
    log.debug(LOG_DEBUG_HEADER_REQUEST, SagJSONUtil.prettyLogJson(httpEntity.getHeaders()));
    final Object httpEntityBody = httpEntity.getBody();
    if (Objects.nonNull(httpEntityBody)) {
      log.info(LOG_INFO_BODY_REQUEST, SagJSONUtil.prettyLogJson(httpEntityBody));
    }
    final ResponseEntity<T> responseEntity =
        restTemplate.exchange(url, method, httpEntity, responseClazz);
    log.info(LOG_INFO_BODY_RESPONSE, SagJSONUtil.prettyLogJson(responseEntity.getBody()));

    return responseEntity;
  }
}
