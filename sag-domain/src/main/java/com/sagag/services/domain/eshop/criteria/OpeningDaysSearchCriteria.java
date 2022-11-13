package com.sagag.services.domain.eshop.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningDaysSearchCriteria implements Serializable {

  private static final long serialVersionUID = -8809711288178249880L;

  @NotNull(message = "The 'dateFrom' must not be null")
  private String dateFrom;

  @NotNull(message = "The 'dateTo' must not be null")
  private String dateTo;

  private String countryName;

  private String workingDayCode;

  private String expAffiliate;

  private String expBranchInfo;

  private String expWorkingDayCode;

  private String expDeliveryAddressId;

  private Boolean orderDescByDate;

  private Boolean orderDescByCountryName;

}
