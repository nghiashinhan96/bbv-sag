package com.sagag.services.ax.availability.filter;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AvailabilityCriteria {

  private ErpSendMethodEnum sendMethodEnum;

  private List<TourTimeDto> tourTimeList;

  private String pickupBranchId;

  private SupportedAffiliate affiliate;

  private Date nextWorkingDate;

  private DateTime lastTourTime;

  private List<WorkingHours> openingHours;

  private String countryName;
}
