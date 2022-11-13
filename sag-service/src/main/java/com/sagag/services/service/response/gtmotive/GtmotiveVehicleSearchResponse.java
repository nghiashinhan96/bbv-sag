package com.sagag.services.service.response.gtmotive;

import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class GtmotiveVehicleSearchResponse implements Serializable {

  private static final long serialVersionUID = -2167935152151150614L;

  private final GtmotiveVehicleDto gtmotiveResponse;

  private final VehicleDto vehicle;

  private final String estimateId;
}
