package com.sagag.services.tools.domain.elasticsearch;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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

  @JsonIgnore
  private Map<String, Object> additionalProperties;
}
