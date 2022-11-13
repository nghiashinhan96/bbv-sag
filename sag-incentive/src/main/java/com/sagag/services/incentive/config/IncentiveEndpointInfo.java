package com.sagag.services.incentive.config;

import lombok.Data;

@Data
public class IncentiveEndpointInfo {

  private String url;

  private String secureKey;

  private String accessPoint;

  private long sessionTimeout;

}
