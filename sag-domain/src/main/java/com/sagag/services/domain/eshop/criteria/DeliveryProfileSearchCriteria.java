package com.sagag.services.domain.eshop.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DeliveryProfileSearchCriteria extends BaseRequest implements Serializable {

  private static final long serialVersionUID = 674776130401574395L;

  private String country;
  private String deliveryProfileName;
  private String distributionBranchCode;
  private String deliveryBranchCode;
  private String vendorCutOffTime;
  private String lastestTime;
  private String lastDelivery;
  private Integer deliveryDuration;
  private DeliveryProfileSearchSortableColumn sort;
}
