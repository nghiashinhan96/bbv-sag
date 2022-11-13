package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleUsageItemDto implements Comparable<VehicleUsageItemDto>, Serializable {

  private static final long serialVersionUID = -6664591586557493375L;

  private String vehId;

  private String vehicleDisplayName;

  private String vehicleBrand;

  private Integer sort;

  @Override
  public int compareTo(VehicleUsageItemDto compareItem) {
    if (this.sort == null) {
      return -1;
    }
    return this.sort.compareTo(compareItem.getSort());
  }
}
