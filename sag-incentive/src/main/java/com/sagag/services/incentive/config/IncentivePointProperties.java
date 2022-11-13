package com.sagag.services.incentive.config;

import lombok.Data;

@Data
public class IncentivePointProperties {

  private String url;
  
  private String authUrl;
  
  private String tokenUrl;

  private String secureKey;

  private String shopValue;

  private String accessPointKey;

  private long sessionTimeoutMs;

  private String hashEncrypt;

}
