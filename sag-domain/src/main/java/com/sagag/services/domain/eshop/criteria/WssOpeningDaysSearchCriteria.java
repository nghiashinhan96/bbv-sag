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
public class WssOpeningDaysSearchCriteria implements Serializable {

  private static final long serialVersionUID = -8105901268575071163L;

  @NotNull(message = "The 'dateFrom' must not be null")
  private String dateFrom;

  @NotNull(message = "The 'dateTo' must not be null")
  private String dateTo;

  private String countryName;

  private String workingDayCode;

  private String expBranchInfo;

  private String expWorkingDayCode;

  private Boolean orderDescByDate;

  private Boolean orderDescByCountryName;

  private Integer orgId;

}
