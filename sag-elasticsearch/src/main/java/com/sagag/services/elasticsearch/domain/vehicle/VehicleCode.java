package com.sagag.services.elasticsearch.domain.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder(
    { 
      "id_veh_code", 
      "veh_code_type", 
      "veh_code_attr", 
      "veh_code_value" 
    })
public class VehicleCode implements Serializable {

  private static final long serialVersionUID = 2684953761893164335L;

  @JsonProperty("id_veh_code")
  private String idVehCode;

  @JsonProperty("veh_code_type")
  private String vehCodeType;

  @JsonProperty("veh_code_attr")
  private String vehCodeAttr;

  @JsonProperty("veh_code_value")
  private String vehCodeValue;

}
