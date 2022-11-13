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
    { "vehid", 
      "vehicle_display_name",
      "vehicle_brand",
      "sort"
    })
//@formatter:on
public class VehicleUsage implements Serializable {

  private static final long serialVersionUID = -5121288037846009095L;

  @JsonProperty("vehid")
  private String vehId;

  @JsonProperty("vehicle_display_name")
  private String vehicleDisplayName;

  @JsonProperty("vehicle_brand")
  private String vehicleBrand;

  @JsonProperty("sort")
  private Integer sort;

}
