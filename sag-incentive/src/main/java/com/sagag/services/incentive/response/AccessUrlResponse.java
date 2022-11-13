package com.sagag.services.incentive.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccessUrlResponse {
  
  @JsonProperty("token")
  private String token;
  
  @JsonProperty("url")
  private String url;
  
  @JsonProperty("redirect_url")
  private String redirectUrl;
  
  @JsonProperty("redirect_url_fragment")
  private String redirectUrlFragment;

}
