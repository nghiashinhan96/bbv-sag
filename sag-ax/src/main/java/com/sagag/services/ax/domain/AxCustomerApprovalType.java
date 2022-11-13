package com.sagag.services.ax.domain;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
	* Class to receive the AxCustomerApprovalType info from Dynamic AX ERP.
	*/
@Data
@EqualsAndHashCode(callSuper = false)
public class AxCustomerApprovalType implements Serializable {

  private static final long serialVersionUID = -2121458646553616768L;

  private String approvalTypeName;

  private String certificate;

}
