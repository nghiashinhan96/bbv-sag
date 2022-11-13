package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleUsageDto implements Serializable {

  private static final long serialVersionUID = 9034298930871591546L;

  private String vehicleBrand;

  private List<VehicleUsageItemDto> vehicleUsage;

}
