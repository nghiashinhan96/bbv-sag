package com.sagag.services.ax.domain.wint;

import java.io.Serializable;

import lombok.Data;

@Data
public class WtGrantedBranch implements Serializable {

  private static final long serialVersionUID = 410148483849465537L;

  private String branchId;

  private String paymentMethodAllowed;

  private Integer orderingPriority;

}
