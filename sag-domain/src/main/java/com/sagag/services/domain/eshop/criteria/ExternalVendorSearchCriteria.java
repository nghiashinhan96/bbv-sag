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
public class ExternalVendorSearchCriteria extends BaseRequest implements Serializable {

  private static final long serialVersionUID = -540016910728441082L;
  private String country;
  private Long vendorId;
  private String vendorName;
  private Integer vendorPriority;
  private String deliveryProfileName;
  private String availabilityTypeId;
  private ExternalVendorSearchSortableColumn sort;
}
