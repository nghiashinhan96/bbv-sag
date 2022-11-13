package com.sagag.services.domain.sag.erp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemApprovalTypeName implements Serializable {

  private static final long serialVersionUID = 4890157990429722369L;

  private String approvalTypename;

}