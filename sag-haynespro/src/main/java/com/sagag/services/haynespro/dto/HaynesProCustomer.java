package com.sagag.services.haynespro.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "customer")
public class HaynesProCustomer {

  private String username;

  private String customerId;

  private String login;

  private String productId;

}
