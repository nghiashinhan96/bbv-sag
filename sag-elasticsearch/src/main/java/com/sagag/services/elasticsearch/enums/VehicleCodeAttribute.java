package com.sagag.services.elasticsearch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VehicleCodeAttribute {

  TYPENSCHEIN("typenschein"),
  NATIONAL_CODE("at_natcode"),
  VEHICLE_KBA_CODE("vehicle_kba_code"),
  CNIT("cnit");

  private String attr;

}
