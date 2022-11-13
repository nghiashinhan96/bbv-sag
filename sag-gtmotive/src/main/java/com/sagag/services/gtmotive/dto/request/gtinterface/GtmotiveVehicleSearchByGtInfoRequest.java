package com.sagag.services.gtmotive.dto.request.gtinterface;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GtmotiveVehicleSearchByGtInfoRequest {

  private String umc;

  private String gtMod;

  private String gtEng;

  private String gtDrv;
}
