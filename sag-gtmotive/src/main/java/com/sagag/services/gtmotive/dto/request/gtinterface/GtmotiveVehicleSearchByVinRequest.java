package com.sagag.services.gtmotive.dto.request.gtinterface;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GtmotiveVehicleSearchByVinRequest {

  private String estimateId;
  private String vin;
}
