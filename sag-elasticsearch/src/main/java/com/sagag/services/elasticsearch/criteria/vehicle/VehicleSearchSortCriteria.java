package com.sagag.services.elasticsearch.criteria.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.sort.SortOrder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleSearchSortCriteria {

  @JsonProperty("vehicle_info")
  private SortOrder vehInfo;

  @JsonProperty("built_year_month_from")
  private SortOrder vehBuiltYearMonthFrom;

  @JsonProperty("body_type")
  private SortOrder vehBodyType;

  @JsonProperty("fuel_type")
  private SortOrder vehFuelType;

  @JsonProperty("zylinder")
  private SortOrder vehZylinder;

  @JsonProperty("engine")
  private SortOrder vehEngine;

  @JsonProperty("motor_code")
  private SortOrder vehMotorCode;

  @JsonProperty("power")
  private SortOrder vehPower;

  @JsonProperty("drive_type")
  private SortOrder vehDriveType;

  @JsonProperty("vehicle_advance_name")
  private SortOrder vehAdvanceName;

  @JsonProperty("capacity_cc_tech")
  private SortOrder vehCapacityCcTech;
}
