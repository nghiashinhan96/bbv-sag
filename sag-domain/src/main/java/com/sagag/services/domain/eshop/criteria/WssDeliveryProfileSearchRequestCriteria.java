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
public class WssDeliveryProfileSearchRequestCriteria implements Serializable {

  private static final long serialVersionUID = 5349789832542618610L;

  private Integer orgId;

  private Boolean orderDescByProfileName;

  private Boolean orderDescByProfileDescription;

  private Boolean orderDescByBranchCode;

}