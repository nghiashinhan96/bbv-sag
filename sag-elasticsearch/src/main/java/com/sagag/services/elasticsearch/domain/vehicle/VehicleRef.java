package com.sagag.services.elasticsearch.domain.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder(
    { "vehid",
      "vehicle_drive_type",
      "sort",
      "ktype",
      "id_make",
      "id_model",
      "id_motor",
      "vehicle_name",
      "vehicle_brand",
      "vehicle_model",
      "vehicle_built_year_from",
      "vehicle_built_year_till",
      "vehicle_body_type",
      "vehicle_fuel_type",
      "vehicle_engine_code",
      "vehicle_power_hp",
      "vehicle_power_kw",
      "vehicle_engine",
      "vehicle_capacity_cc_tech",
      "vehicle_zylinder",
      "gt_drv",
      "gt_umc",
      "gt_mod",
      "gt_eng",
      "vehicleInfo"
    })
//@formatter:on
public class VehicleRef implements Serializable {

  private static final long serialVersionUID = -5121288037846009095L;

  @JsonProperty("vehid")
  private String vehId;

  @JsonProperty("vehicle_drive_type")
  private String vehicleDriveType;

  @JsonProperty("sort")
  private Integer sort;

  @JsonProperty("ktype")
  private Integer ktType;

  @JsonProperty("id_make")
  private Integer idMake;

  @JsonProperty("id_model")
  private Integer idModel;

  @JsonProperty("id_motor")
  private Integer idMotor;

  @JsonProperty("vehicle_name")
  private String vehicleName;

  @JsonProperty("vehicle_brand")
  private String vehicleBrand;

  @JsonProperty("vehicle_model")
  private String vehicleModel;

  @JsonProperty("vehicle_built_year_from")
  private String vehicleBuiltYearFrom;

  @JsonProperty("vehicle_built_year_till")
  private String vehicleBuiltYearTill;

  @JsonProperty("vehicle_body_type")
  private String vehicleBodyType;

  @JsonProperty("vehicle_fuel_type")
  private String vehicleFuelType;

  @JsonProperty("vehicle_engine_code")
  private String vehicleEngineCode;

  @JsonProperty("vehicle_power_hp")
  private String vehiclePowerHp;

  @JsonProperty("vehicle_power_kw")
  private String vehiclePowerKw;

  @JsonProperty("vehicle_engine")
  private String vehicleEngine;

  @JsonProperty("vehicle_capacity_cc_tech")
  private Integer vehicleCapacityCcTech;

  @JsonProperty("vehicle_zylinder")
  private Integer vehicleZylinder;

  @JsonProperty("gt_drv")
  @Field(analyzer = "not_analyzed")
  private String gtDrv;

  @JsonProperty("gt_umc")
  @Field(analyzer = "not_analyzed")
  private String gtUmc;

  @JsonProperty("gt_mod")
  @Field(analyzer = "not_analyzed")
  private String gtMod;

  @JsonProperty("gt_eng")
  @Field(analyzer = "not_analyzed")
  private String gtEng;

  @JsonProperty("vehicleInfo")
  @Field(analyzer = "vehicleInfo")
  private String vehicleInfo;

}
