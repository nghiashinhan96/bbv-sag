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
public class AllocationTypeDto implements Serializable {

  private static final long serialVersionUID = -3506928523760404340L;

  private int id;

  private String descCode;

  private String type;

  private String description;

  private boolean allowChoose;

  public AllocationTypeDto(int id, String descCode, String description, String type) {
    this.id = id;
    this.descCode = descCode;
    this.description = description;
    this.type = type;
  }

}
