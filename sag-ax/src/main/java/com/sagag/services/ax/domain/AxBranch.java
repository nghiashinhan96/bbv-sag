package com.sagag.services.ax.domain;


import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.sag.external.CustomerBranch;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the branch info from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxBranch extends AxBaseResponse {

  private static final long serialVersionUID = -7218357626940923318L;

  private String branchId;

  private String branchName;

  private String phoneNr;

  @JsonIgnore
  public boolean hasBranch() {
    return StringUtils.isNotBlank(branchId);
  }

  @JsonIgnore
  public CustomerBranch toCustomerBranchDto() {
    return CustomerBranch.builder().branchId(branchId).branchName(branchName)
        .phoneNr(phoneNr)
        .build();
  }

}
