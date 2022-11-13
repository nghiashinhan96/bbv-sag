package com.sagag.services.autonet.erp.converter;

import com.sagag.services.autonet.erp.enums.AutonetArticleAvailabilityState;
import com.sagag.services.autonet.erp.wsdl.tmconnect.AvailabilityStateType;
import com.sagag.services.domain.sag.erp.ErpArticleAvailability;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AutonetAvailabilityStateConverter {

  public static ErpArticleAvailability fromAvailabilityStateType(
      AvailabilityStateType availabilityStateType) {
    AutonetArticleAvailabilityState state =
        AutonetArticleAvailabilityState.fromCode(availabilityStateType.getType());
    final ErpArticleAvailability erpArtAvail = new ErpArticleAvailability();
    erpArtAvail.setAvailState(state.getCode());
    erpArtAvail.setAvailStateColor(state.name());
    return erpArtAvail;
  }
}
