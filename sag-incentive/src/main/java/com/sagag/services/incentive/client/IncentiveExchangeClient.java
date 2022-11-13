package com.sagag.services.incentive.client;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.incentive.config.IncentiveProfile;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@Slf4j
@IncentiveProfile
public class IncentiveExchangeClient {

  @Autowired
  @Qualifier("incentiveRestTemplate")
  private RestTemplate restTemplate;

  @LogExecutionTime(
      value = "Perf:Happy bonus ExchangeClient -> exchange -> Execute Happy bonus API in {} ms",
      infoMode = true)
  public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> httpEntity,
      Class<T> responseClazz) {
    log.info("Happy bonus request: URL = {}\nHeaders = {} ", url, httpEntity.getHeaders());
    if (Objects.nonNull(httpEntity.getBody())) {
      log.info("Body request: \n{}", SagJSONUtil.convertObjectToPrettyJson(httpEntity.getBody()));
    }
    final ResponseEntity<T> responseEntity =
        restTemplate.exchange(url, method, httpEntity, responseClazz);
    log.info("Body Response: \n{}", SagJSONUtil.convertObjectToPrettyJson(responseEntity));
    return responseEntity;
  }

}
