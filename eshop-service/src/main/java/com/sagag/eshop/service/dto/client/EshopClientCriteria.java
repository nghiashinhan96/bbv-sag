package com.sagag.eshop.service.dto.client;

import com.sagag.services.domain.eshop.common.EshopAuthority;

import lombok.Data;

import java.util.List;

@Data
public class EshopClientCriteria {

  private String clientName;

  private String resourceName;

  private List<EshopAuthority> allowedAuthorities;

}
