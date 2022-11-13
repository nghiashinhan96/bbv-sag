package com.sagag.eshop.service.dto.sso;

import lombok.Data;

import java.io.Serializable;

@Data
public class SsoLoginProfileResponseDto implements Serializable {

  private static final long serialVersionUID = 6291551500550094641L;
  private String username;
}
