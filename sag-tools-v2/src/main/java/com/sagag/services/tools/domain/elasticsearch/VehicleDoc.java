package com.sagag.services.tools.domain.elasticsearch;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(
  indexName = "vehicles_de",
  type = "refs",
  refreshInterval = "-1",
  createIndex = false,
  useServerConfiguration = true
)
public class VehicleDoc implements Serializable {

  private static final long serialVersionUID = 3098723368643498993L;

  @Id
  @JsonProperty("id")
  private String id;

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

  @JsonProperty("id_sag")
  private String idSag;

  @JsonProperty("id_sag_make")
  private String idSagMake;

  @JsonProperty("id_sag_model")
  private String idSagModel;

  @JsonProperty("vehicleInfo")
  @Field(analyzer = "vehicleInfo")
  private String vehicleInfo;

  @JsonProperty("codes")
  @Field(type = FieldType.Nested)
  private List<VehicleCode> codes;

  @JsonProperty("gt_links")
  @Field(type = FieldType.Nested)
  private List<GtMotiveLink> gtLinks;

  @JsonIgnore
  private Map<String, Object> additionalProperties;

  @JsonProperty("is_electric")
  private Integer isElectric;
}
