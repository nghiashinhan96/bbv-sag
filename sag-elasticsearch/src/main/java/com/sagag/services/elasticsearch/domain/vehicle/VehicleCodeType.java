package com.sagag.services.elasticsearch.domain.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VehicleCodeType {

  GTMOTIVE("gtmotive"), TYPENSCHEIN("typs_ch");

  private String type;

}
