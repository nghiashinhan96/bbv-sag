package com.sagag.services.domain.eshop.branch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.domain.eshop.dto.BranchOpeningTimeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BranchRequestBody implements Serializable {

  private static final long serialVersionUID = -8809711288178249880L;

  @NotNull(message = "Branch number must not be null")
  private Integer branchNr;

  @Size(min = 0, max = 10)
  private String branchCode;

  private String openingTime;

  private String closingTime;

  private String lunchStartTime;

  private String lunchEndTime;

  private String addressStreet;

  private String addressCity;

  private String addressDesc;

  private String addressCountry;

  private Integer countryId;

  private String zip;

  private Integer orgId;

  private String regionId;

  private String primaryPhone;

  private String primaryFax;

  private String primaryEmail;

  private Boolean validForKSL;

  private String primaryUrl;

  private Boolean hideFromCustomers;

  private Boolean hideFromSales;

  @Valid
  private List<BranchOpeningTimeDto> branchOpeningTimes;

}
