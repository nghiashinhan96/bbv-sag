package com.sagag.services.hazelcast.domain.haynespro;

import lombok.Data;

import java.io.Serializable;

@Data
public class HaynesProCacheCustomerDto implements Serializable {

  private static final long serialVersionUID = -9087610412913678493L;

  private String username;

  private String customerId;

  private String login;

  private String productId;
}
