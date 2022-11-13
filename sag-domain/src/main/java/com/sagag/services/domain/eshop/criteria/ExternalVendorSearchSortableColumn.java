package com.sagag.services.domain.eshop.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExternalVendorSearchSortableColumn implements Serializable {

  private static final long serialVersionUID = 4426441392579203553L;
  private Boolean orderByCountryDesc;
  private Boolean orderByVendorIdDesc;
  private Boolean orderByVendorNameDesc;
  private Boolean orderByVendorPriorityDesc;
  private Boolean orderByDeleviryProfileNameDesc;
  private Boolean orderByAvailabilityTypeDesc;

}
