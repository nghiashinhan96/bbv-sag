package com.sagag.services.dvse.domain;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class WebSocketConfigInfo {

  private String endpoint;

  public boolean isOn() {
    return !StringUtils.isBlank(getEndpoint());
  }

}
