package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
public class TypeItem implements Comparable<TypeItem> {

  private String vehId;

  private String vehicleName;

  private String vehicleEngineCode;

  private String vehiclePowerKw;

  private String vehicleEngine;

  private Integer vehicleCapacityCcTech;

  private String vehiclePowerHp;

  @Setter
  private String displayType;

  private Integer sort;


  public String getDisplayType() {
    return String.format("%s %s kW %s", vehicleName, vehiclePowerKw, vehicleEngineCode);
  }

  @Override
  public int compareTo(TypeItem compareItem) {
    if (this.sort == null) {
      return -1; // move to the end
    }
    return this.sort.compareTo(compareItem.getSort());
  }
}
