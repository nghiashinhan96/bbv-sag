package com.sagag.services.domain.eshop.dto.externalvendor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.domain.eshop.branch.dto.BranchRequestBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class SupportDeliveryProfileDto implements Serializable {

  private static final long serialVersionUID = 5576785549449032113L;

  private List<CountryDto> countries;
  private List<BranchRequestBody> branchNr;
  private List<BranchRequestBody> deliveryBranchSearch;
  private List<BranchRequestBody> distributionBranchSearch;

}
