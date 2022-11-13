package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@AllArgsConstructor
public class MakeItem {

  private Integer makeId;

  private String make;

  private String gtMake;

  private boolean gtVin;

  private boolean normauto;

  private String vehicleType;

  private boolean top;

  @JsonIgnore
  public String getDefaultMakeIdStr() {
    if (makeId == null) {
      return StringUtils.EMPTY;
    }
    return String.valueOf(makeId);
  }
}
