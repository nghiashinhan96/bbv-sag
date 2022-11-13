package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleCodeDto implements Serializable {

  private static final long serialVersionUID = 5117696034640940379L;

  @JsonProperty("id_veh_code")
  private String idVehCode;

  @JsonProperty("veh_code_type")
  private String vehCodeType;

  @JsonProperty("veh_code_attr")
  private String vehCodeAttr;

  @JsonProperty("veh_code_value")
  private String vehCodeValue;

}
