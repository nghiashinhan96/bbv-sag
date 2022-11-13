package com.sagag.services.rest.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.Map;

/**
 * Token definition class.
 */
@Data
public class TokenResult {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("expires_in")
  private long expiresIn;

  private String scope;

  private String jti;

  @JsonProperty("details")
  private Map<String, Object> details;
}
