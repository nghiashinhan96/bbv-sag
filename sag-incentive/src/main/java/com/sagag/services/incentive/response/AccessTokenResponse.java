package com.sagag.services.incentive.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccessTokenResponse {

  @JsonProperty("token_type")
  private String tokenType;
  
  @JsonProperty("expires_in")
  private String expiresIn;
  
  @JsonProperty("access_token")
  private String accessToken;


}
