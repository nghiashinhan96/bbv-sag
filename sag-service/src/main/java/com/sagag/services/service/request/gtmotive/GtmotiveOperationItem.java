package com.sagag.services.service.request.gtmotive;

import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GtmotiveOperationItem implements Serializable {

  private static final long serialVersionUID = -2765458142769097840L;

  private String reference;

  private String description;

  private String auxiliarInformation;

  private String supplyType;

  public static GtmotiveOperationItem fromGtmotivePartsThreeReference(
      GtmotivePartsThreeReference gtmotivePartsThreeReference) {
    return GtmotiveOperationItem.builder().reference(gtmotivePartsThreeReference.getCode())
        .description(gtmotivePartsThreeReference.getDescription())
        .auxiliarInformation(gtmotivePartsThreeReference.getAuxiliarInformation())
        .supplyType(gtmotivePartsThreeReference.getSupplyType()).build();
  }
}
