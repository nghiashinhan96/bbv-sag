package com.sagag.services.elasticsearch.criteria.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleFilteringCriteria {

  @JsonProperty("built_year_month_from")
  private String vehBuiltYearMonthFrom;

  @JsonProperty("body_type")
  private String vehBodyType;

  @JsonProperty("fuel_type")
  private String vehFuelType;

  @JsonProperty("zylinder")
  private Integer vehZylinder;

  @JsonProperty("engine")
  private String vehEngine;

  @JsonProperty("motor_code")
  private String vehMotorCode;

  @JsonProperty("power")
  private String vehPower;

  @JsonProperty("drive_type")
  private String vehDriveType;

  @JsonProperty("full_name")
  private String vehFullName;

  @JsonProperty("model_gen")
  private String modelGen;

  @JsonProperty("model_series")
  private String modelSeries;

  @JsonProperty("vehicle_advance_name")
  private String vehAdvanceName;

  @JsonProperty("capacity_cc_tech")
  private String vehCapacityCcTech;
}
