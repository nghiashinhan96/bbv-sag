package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTypeDto implements Serializable{

  private static final long serialVersionUID = -2504101374036288565L;

  private int id;

  private String descCode;

  private String type;

  private String description;

  private boolean allowChoose;
}
