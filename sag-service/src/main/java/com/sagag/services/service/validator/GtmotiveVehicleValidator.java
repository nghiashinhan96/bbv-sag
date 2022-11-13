package com.sagag.services.service.validator;

import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.gtmotive.domain.response.EquipmentItem;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleInfo;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVehicleInfoResponse;
import com.sagag.services.gtmotive.exception.GtmotiveNotFoundVehicleException;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;
import com.sagag.services.ivds.api.IvdsVehicleService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class GtmotiveVehicleValidator implements IDataValidator<GtmotiveVehicleInfoResponse> {

  @Autowired
  private IvdsVehicleService ivdsVehicleService;

  @Override
  public boolean validate(GtmotiveVehicleInfoResponse response) throws ValidationException {
    GtmotiveVehicleInfo vehicleInfo = response.getVehicleInfo();
    List<EquipmentItem> equipment = vehicleInfo.getEquipments();

    boolean hasValidMOT = false;
    boolean hasValidCAM = false;
    for (EquipmentItem equipmentItem : equipment) {
      log.debug("equipmentItem {} {} {}", equipmentItem.getFamily(), equipmentItem.getSubFamily(),
          equipmentItem.getSource());
      if (GtmotiveUtils.GT_SUB_FAMILY_ALLOWED.contains(equipmentItem.getSubFamily())
          && GtmotiveUtils.GT_FAMILY_MOT.equals(equipmentItem.getFamily())
          && GtmotiveUtils.GT_VIN_QUERY.equals(equipmentItem.getSource())) {
        hasValidMOT = true;
        log.debug("equipment has valid MOT");
      } else if (GtmotiveUtils.GT_FAMILY_CAM.equals(equipmentItem.getFamily())
          && (GtmotiveUtils.GT_VIN_QUERY.equals(equipmentItem.getSource())
              || GtmotiveUtils.AUTOMATIC.equals(equipmentItem.getSource()))) {
        log.debug("equipment has valid CAM");
        hasValidCAM = true;
      }
    }
    // Check UMC in ES
    Optional<GtmotiveVehicleDto> gtmotiveVehicleDtoOpt =
        GtmotiveVehicleDto.converter().apply(response);
    if (!gtmotiveVehicleDtoOpt.isPresent()) {
      return false;
    }

    GtmotiveVehicleDto gtmotiveVehicleDto = gtmotiveVehicleDtoOpt.get();
    final String umc = gtmotiveVehicleDto.getUmc();
    final List<String> models = gtmotiveVehicleDto.getModelCodes();
    final List<String> engines = gtmotiveVehicleDto.getEngineCodes();
    final List<String> transmisions = gtmotiveVehicleDto.getTransmisionCodes();

    final Optional<VehicleDto> vehicleOpt = ivdsVehicleService.searchGtmotiveVehicle(umc, models,
        engines, transmisions, StringUtils.EMPTY);

    if (!vehicleOpt.isPresent()) {
      log.warn(
          "Not found vehicle in ES with umc = {}, models = {}, engines = {}, transmisions = {} ",
          umc, models, engines, transmisions);
      throw new GtmotiveNotFoundVehicleException("Not found vehicle in ES");
    }

    return isValidGtVehicle(vehicleOpt.get(), hasValidMOT, hasValidCAM);

  }

  private boolean isValidGtVehicle(final VehicleDto gtmotiveVehicle, final boolean hasValidMOT,
      final boolean hasValidCAM) {
    if (!Objects.isNull(gtmotiveVehicle) && !Objects.isNull(gtmotiveVehicle.getIsElectric())
        && gtmotiveVehicle.getIsElectric() == 1) {
      log.debug("Vehicle has Electric mode");
      return true;
    }
    if (!hasValidMOT) {
      return false;
    }
    return hasValidCAM;
  }
}
