package com.sagag.services.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.elasticsearch.domain.article.GTMotiveLink;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreetextVehicleDto implements Serializable {

  private static final long serialVersionUID = -2000591101822767623L;

  private static final String KW = "kW";

  private String vehId;

  private String vehCodeType;

  private String gtStart;

  private String gtEnd;

  private String gtModAlt;

  private String gtmodName;

  private String gtMakeCode;

  private String gtModelCode;

  private String gtEngineCode;

  private String transmisionCode;

  private String vehMakeId;

  private String vehModelId;

  private String vehMotorId;

  @Setter
  private String vehDisplay;

  private String vehBranch;

  private String vehModel;

  private String vehName;

  private String vehPowerKw;

  private String vehEngineCode;

  private String vehBuiltYearFrom;

  private String vehBuiltYearTo;

  private String vehFuelType;

  private Integer vehZylinder;

  private String vehEngine;

  private String vehBodyType;

  private String vehDriveType;

  @Setter
  private String vehTypeDesc;

  private Integer ktType;

  public String getVehDisplay() {
    return StringUtils.join(new String[] { vehBranch, vehModel, getVehTypeDesc() },
        SagConstants.SPACE);
  }

  public String getVehTypeDesc() {
    return StringUtils.join(new String[] { vehName, vehPowerKw, KW, vehEngineCode },
        SagConstants.SPACE);
  }

  /**
   * Copies properties from vehicle documents to the vehicle data transfer object.
   *
   * @param veh the vehicle document
   * @return a vehicle with necessary properties
   */
  public static FreetextVehicleDto copy(final VehicleDoc veh) {
    final FreetextVehicleDto vehicle = new FreetextVehicleDto();
    if (Objects.isNull(veh)) {
      return null;
    }
    List<GTMotiveLink> gtLinks = veh.getGtLinks();
    if (CollectionUtils.isNotEmpty(gtLinks)) {
      GTMotiveLink firstGtmotiveLink = gtLinks.stream().findFirst().orElse(new GTMotiveLink());
      vehicle.setGtMakeCode(firstGtmotiveLink.getGtMakeCode());
      vehicle.setGtModelCode(firstGtmotiveLink.getGtModelCode());
      vehicle.setGtEngineCode(firstGtmotiveLink.getGtEngineCode());
      vehicle.setTransmisionCode(firstGtmotiveLink.getTransmisionCode());
      vehicle.setGtStart(firstGtmotiveLink.getGtStart());
      vehicle.setGtEnd(firstGtmotiveLink.getGtEnd());
      vehicle.setGtModAlt(firstGtmotiveLink.getGtModAlt());
      vehicle.setGtmodName(firstGtmotiveLink.getGtmodName());
    }
    vehicle.setVehBranch(veh.getVehicleBrand());
    vehicle.setVehEngineCode(veh.getVehicleEngineCode());
    vehicle.setVehId(veh.getVehId());
    vehicle.setVehModel(veh.getVehicleModel());
    final Integer makeId = veh.getIdMake();
    vehicle.setVehMakeId(makeId == null ? StringUtils.EMPTY : String.valueOf(makeId));
    final Integer modelId = veh.getIdModel();
    vehicle.setVehModelId(modelId == null ? StringUtils.EMPTY : String.valueOf(modelId));
    final Integer motorId = veh.getIdMotor();
    vehicle.setVehMotorId(motorId == null ? StringUtils.EMPTY : String.valueOf(motorId));
    vehicle.setVehName(veh.getVehicleName());
    vehicle.setVehPowerKw(veh.getVehiclePowerKw());
    vehicle.setVehBuiltYearFrom(veh.getVehicleBuiltYearFrom());
    vehicle.setVehBuiltYearTo(veh.getVehicleBuiltYearTill());
    vehicle.setVehFuelType(veh.getVehicleFuelType());
    vehicle.setVehZylinder(veh.getVehicleZylinder());
    vehicle.setVehEngine(veh.getVehicleEngine());
    vehicle.setVehBodyType(veh.getVehicleBodyType());
    vehicle.setVehDriveType(veh.getVehicleDriveType());
    vehicle.setKtType(veh.getKtType());
    return vehicle;
  }

}
