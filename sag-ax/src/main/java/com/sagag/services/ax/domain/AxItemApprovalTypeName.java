package com.sagag.services.ax.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the AxItemApprovalTypeName info from Dynamic AX ERP.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxItemApprovalTypeName implements Serializable {

  private static final long serialVersionUID = -559361181337409604L;

  @JsonProperty("approvaltypename")
  private String approvalTypename;

}
