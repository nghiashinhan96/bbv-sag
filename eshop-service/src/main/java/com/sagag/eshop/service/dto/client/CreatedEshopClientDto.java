package com.sagag.eshop.service.dto.client;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreatedEshopClientDto implements Serializable {

  private static final long serialVersionUID = -5875413376759290066L;

  private int id;

  private String clientName;

  private String clientSecret;

}
