package com.sagag.services.domain.eshop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleModelYear {

  private String year;

  private String vehId;

}
