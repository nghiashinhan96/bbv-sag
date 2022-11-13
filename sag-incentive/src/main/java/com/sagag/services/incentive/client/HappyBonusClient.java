package com.sagag.services.incentive.client;

import com.sagag.services.incentive.config.IncentiveProfile;
import com.sagag.services.incentive.domain.AccessTokenRequest;
import com.sagag.services.incentive.response.AccessTokenResponse;
import com.sagag.services.incentive.response.AccessUrlResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@IncentiveProfile
public class HappyBonusClient {

  @Autowired
  private IncentiveExchangeClient incentiveExchangeClient;

  public AccessTokenResponse getAccessToken(final String url,
      final AccessTokenRequest tokenRequestBody) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    return incentiveExchangeClient.exchange(url, HttpMethod.POST,
        new HttpEntity<AccessTokenRequest>(tokenRequestBody, httpHeaders),
        AccessTokenResponse.class).getBody();
  }

  public AccessUrlResponse getAccessUrl(final String url, String accessToken) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    return incentiveExchangeClient
        .exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), AccessUrlResponse.class)
        .getBody();
  }



}
