package com.sagag.services.autonet.erp.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class AutonetErpUserInfo implements Serializable {

  private static final long serialVersionUID = -5275341748863205667L;

  private String username;

  private String customerId;

  private String lang;

  private String requestId;

  private String securityToken;
}
