package com.sagag.services.tools.domain.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBranch implements Serializable {

  private static final long serialVersionUID = 4151771455157965932L;

  private String branchId;

  private String branchName;

}
