package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleRefDto implements Serializable {

  private static final long serialVersionUID = 4269914343983936591L;

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
  private String gtDrv;

  @JsonProperty("gt_umc")
  private String gtUmc;

  @JsonProperty("gt_mod")
  private String gtMod;

  @JsonProperty("gt_eng")
  private String gtEng;

  @JsonProperty("vehicleInfo")
  private String vehicleInfo;
}
