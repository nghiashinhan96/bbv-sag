package com.sagag.services.rest.controller.analytics;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class DataAnalyticsClient {

  private static final String LOG_MSG = "URL = {} \nHeaders = {} \nRequest body = {}";

  @Value("${external.webservice.analytics.url.receive:}")
  private String url;

  @Autowired
  @Qualifier("analyticsRestTemplate")
  private RestTemplate restTemplate;

  public void forwardLog(String body) {
    exchange(body);
  }

  private void exchange(final String body) {
    if (StringUtils.isBlank(url)) {
      log.debug("The data analytics feature is off");
      return;
    }

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    headers.setCacheControl(CacheControl.noCache());

    final HttpEntity<String> entity = new HttpEntity<>(body, headers);
    log.debug(LOG_MSG, url, entity.getHeaders(), entity.getBody());
    try {
      ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
      log.debug("Response status: {}", responseEntity);
    } catch (RestClientException ex) {
      log.error("Analytics server error: ", ex);
      throw ex;
    }
  }

}
