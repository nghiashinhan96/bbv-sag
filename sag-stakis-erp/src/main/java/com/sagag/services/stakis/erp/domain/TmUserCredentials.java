package com.sagag.services.stakis.erp.domain;

import lombok.Data;

@Data
public class TmUserCredentials {

  private String username;

  private String password;

  private String customerId;

  private String lang;

  private int contextId;

  private String securityToken;

  private String saleOriginId;

  private String salesUsername;
}
