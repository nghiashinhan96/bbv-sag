package com.sagag.services.domain.eshop.branch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BranchSearchRequestCriteria implements Serializable {

  private static final long serialVersionUID = 1L;

  private String branchNr;

  private String branchCode;

  private String openingTime;

  private String closingTime;

  private String lunchStartTime;

  private String lunchEndTime;

  private Boolean hideFromCustomers;

  private Boolean hideFromSales;

  private Boolean orderDescByBranchNr;

  private Boolean orderDescByBranchCode;

  private Boolean orderDescByOpeningTime;

  private Boolean orderDescByClosingTime;

  private Boolean orderDescByLunchStartTime;

  private Boolean orderDescByLunchEndTime;
}