package com.sagag.services.oates.api.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.services.common.utils.VehicleUtils;
import com.sagag.services.oates.api.OatesService;
import com.sagag.services.oates.client.OatesClient;
import com.sagag.services.oates.config.OatesProfile;
import com.sagag.services.oates.converter.OatesApplicationConverter;
import com.sagag.services.oates.domain.OatesEquipmentProducts;
import com.sagag.services.oates.domain.OatesRecommendVehicles;
import com.sagag.services.oates.dto.OatesEquipmentProductsDto;
import com.sagag.services.oates.dto.OatesVehicleDto;

@Service
@OatesProfile
public class OatesServiceImpl implements OatesService {

  @Autowired
  private OatesClient oatesClient;

  @Autowired
  private OatesApplicationConverter oatesApplicationConverter;

  @Override
  public Optional<OatesVehicleDto> searchOatesVehicle(String vehicleId) {
    if (StringUtils.isBlank(vehicleId)) {
      return Optional.empty();
    }
    final String kType = VehicleUtils.extractKTypeFromVehId(vehicleId);
    final OatesRecommendVehicles vehicles = oatesClient.getOatesRecommendVehicles(kType,
        StringUtils.EMPTY, StringUtils.EMPTY);
    if (vehicles == null || !vehicles.isValidOatesVehicle()) {
      return Optional.empty();
    }
    return vehicles.getEquipmentList().getEquipment().stream().findFirst()
        .map(item -> new OatesVehicleDto(item.getHref(), item.getDisplayNameLong()));
  }

  @Override
  public OatesEquipmentProductsDto searchOatesEquipment(String href) {
    final OatesEquipmentProducts equipmentProduct = oatesClient.getOatesRecommendProducts(href);
    return oatesApplicationConverter.apply(equipmentProduct);
  }

}
