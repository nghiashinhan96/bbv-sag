package com.sagag.services.domain.eshop.openingdays.dto;

import com.sagag.services.common.enums.SupportedAffiliate;

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
public class OpeningDaysAvailabilityInfo implements Serializable {

  private static final long serialVersionUID = 1572680957998934554L;

  private SupportedAffiliate affiliate;

  private Date date;

  private String pickupBranchId;

  private String countryName;

}