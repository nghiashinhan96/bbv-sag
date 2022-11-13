package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectiveDeliveryDto implements Serializable {

  private static final long serialVersionUID = 312520771216080547L;

  private int id;

  private String descCode;

  private String type;

  private String description;

  private boolean allowChoose;

  public CollectiveDeliveryDto(int id, String descCode, String description, String type) {
    this.id = id;
    this.descCode = descCode;
    this.description = description;
    this.type = type;
  }

}
