package com.sagag.services.incentive.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessTokenRequest {

  @JsonProperty("grant_type")
  private String grantType;
  
  @JsonProperty("client_id")
  private String clientId;
  
  @JsonProperty("client_secret")
  private String clientSecret;
}
