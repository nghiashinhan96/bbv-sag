package com.sagag.eshop.service.dto;

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
public class OpeningDaysDto implements Serializable {

  private static final long serialVersionUID = -8809711288178249886L;

  private Integer id;

  private Date date;

  private String countryName;

  private String workingDayCode;

  private String expAffiliate;

  private String expBranchInfo;

  private String expWorkingDayCode;

  private String expDeliveryAddressId;

}
