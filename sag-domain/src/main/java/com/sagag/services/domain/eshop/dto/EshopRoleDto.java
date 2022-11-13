package com.sagag.services.domain.eshop.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class EshopRoleDto implements Serializable {

  private static final long serialVersionUID = 2809072943486019068L;
  private int id;
  private String description;
  private String name;
}
