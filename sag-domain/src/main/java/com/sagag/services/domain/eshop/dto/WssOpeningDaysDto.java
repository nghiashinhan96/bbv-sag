package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WssOpeningDaysDto implements Serializable {

  private static final long serialVersionUID = -3743525116754361077L;

  private Integer id;

  private Date date;

  private String countryName;

  private String workingDayCode;

  private String expBranchInfo;

  private String expWorkingDayCode;

}
