package com.sagag.services.domain.sag.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrantedBranch implements Serializable {

  private static final long serialVersionUID = 215772309883847326L;

  private String branchId;

  private String paymentMethodAllowed;

  private Integer orderingPriority;
}
