package com.sagag.services.domain.eshop.openingdays.dto;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.WorkingDayCode;

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
public class OpeningDaysExceptionInfo implements Serializable {

  private static final long serialVersionUID = -8809711288178249886L;

  private SupportedAffiliate affiliate;

  private List<String> branchNrs;

  private WorkingDayCode code;

  private List<String> deliveryAdrressIDs;

}