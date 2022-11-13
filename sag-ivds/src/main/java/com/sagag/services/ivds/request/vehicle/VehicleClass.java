package com.sagag.services.ivds.request.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VehicleClass {
  GENERAL(""), MOTORBIKE("mb");

  public String vehicleClassShortName;

}
