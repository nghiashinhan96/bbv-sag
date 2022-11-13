package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.utils.VehicleUtils;
import com.sagag.services.domain.article.GTMotiveLinkDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleDto implements Serializable {

  private static final long serialVersionUID = 1784175014437447511L;

  private static final String VEHICLE_KBA_CODE = "vehicle_kba_code";

  private static final String KBA_NR = "kbanr";

  @JsonProperty("id")
  private String id;

  @JsonProperty("vehid")
  private String vehId;

  @JsonProperty("vehicle_drive_type")
  private String vehicleDriveType;

  @JsonProperty("vehicle_doors")
  private Integer vehicleDoors;

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

  @JsonProperty("vehicle_valves")
  private Integer vehicleValves;

  @JsonProperty("vehicle_fuel_mixture_formation")
  private String vehicleFuelMixtureFormation;

  @JsonProperty("vehicle_catalyst_converter_type")
  private String vehicleCatalystConverterType;

  @JsonProperty("codes")
  private List<VehicleCodeDto> codes;

  @JsonProperty("gt_links")
  private List<GTMotiveLinkDto> gtLinks;

  @JsonProperty("vehicle_display")
  private String vehicleDisplay;

  @JsonProperty("is_electric")
  private Integer isElectric;

  @JsonProperty("vehicle_class")
  private String vehicleClass;

  @JsonIgnore
  private String vehicleFullName;

  private boolean isVinSearch;

  private boolean isFavorite;

  private String favoriteComment;

  private String vin;

  /**
   * Returns the vehicle info to display on UI.
   *
   * @return the vehicle info.
   */
  //@formatter:off
  public String getVehicleInfo() {
    return StringUtils.defaultIfBlank(getVehicleFullName(), VehicleUtils.buildVehicleInfo(
        getVehicleBrand(), getVehicleModel(), getVehicleName(),getVehiclePowerKw(),
        getVehicleEngineCode()));
  }

  /**
   * Returns the vehicle type description.
   *
   * @return the vehicle type description
   */
  public String getVehTypeDesc() {
    return VehicleUtils.buildVehicleTypeDesc(getVehicleName(),
      getVehiclePowerKw(),
      getVehicleEngineCode());
  }
  //@formatter:on

  @JsonIgnore
  public List<String> getKbaNrs() {
    return ListUtils.emptyIfNull(getCodes()).stream().filter(kbaTypePredicate())
        .map(VehicleCodeDto::getVehCodeValue).collect(Collectors.toList());
  }

  private static Predicate<VehicleCodeDto> kbaTypePredicate() {
    return code -> VEHICLE_KBA_CODE.equalsIgnoreCase(code.getVehCodeAttr())
        && KBA_NR.equalsIgnoreCase(code.getVehCodeType());
  }

  @JsonIgnore
  public String getKTypeNrStr() {
    return getKtType() != null ? String.valueOf(getKtType()) : StringUtils.EMPTY;
  }
}
