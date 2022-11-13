package com.sagag.services.domain.eshop.dto.externalvendor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryProfileSavingDto implements Serializable {

  private static final long serialVersionUID = 6085091963092521662L;

  private Integer id;
  private String country;
  private Integer deliveryProfileId;
  private Integer distributionBranchId;
  private Integer deliveryBranchId;
  private Integer nextDay;
  private String vendorCutOffTime;
  private String lastDelivery;
  private String latestTime;
  private Integer deliveryDuration;
  private String deliveryProfileName;
}
