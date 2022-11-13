package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrantedBranchDto implements Serializable {

  private static final long serialVersionUID = -323954467163579428L;

  private String branchId;

  private String branchName;

  private String paymentMethodAllowed;

  private String locationType;

}
