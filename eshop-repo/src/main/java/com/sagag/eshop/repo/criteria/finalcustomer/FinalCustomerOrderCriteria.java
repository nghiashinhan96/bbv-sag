package com.sagag.eshop.repo.criteria.finalcustomer;

import com.sagag.eshop.repo.criteria.SearchCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FinalCustomerOrderCriteria extends SearchCriteria {

  private static final long serialVersionUID = 6639408994192460456L;

  private List<String> statuses;

  private String customerInfo;

  private Long id;

  private String dateFrom;

  private String dateTo;

  private String username;

  private String vehicleDescs;

  private Boolean orderDescCustomerInfo;

  private Boolean orderDescById;

  private Boolean orderDescByOrderDate;

  private List<Long> finalOrgIds;
}
