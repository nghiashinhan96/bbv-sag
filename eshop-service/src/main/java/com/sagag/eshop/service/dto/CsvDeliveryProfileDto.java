package com.sagag.eshop.service.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.sagag.services.common.utils.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvDeliveryProfileDto implements Serializable {

  private static final long serialVersionUID = 4946250921200950521L;
  private Integer id;

  @CsvBindByName(column = "COUNTRY", required = true)
  private String country;

  @CsvBindByName(column = "DELIVERY_PROFILE_ID", required = true)
  private Integer deliveryProfileId;

  @CsvBindByName(column = "DISTRIBUTION_BRANCH_ID", required = true)
  private Integer distributionBranchId;

  @CsvBindByName(column = "DELIVERY_BRANCH_ID", required = true)
  private Integer deliveryBranchId;

  @CsvBindByName(column = "NEXT_DAY")
  private Integer nextDay;

  @CsvBindByName(column = "VENDOR_CUT_OFF_TIME")
  @CsvDate(DateUtils.SHORT_TIME_PATTERN)
  private Date vendorCutOffTime;

  @CsvBindByName(column = "LAST_DELIVERY", required = true)
  @CsvDate(DateUtils.SHORT_TIME_PATTERN)
  private Date lastDelivery;

  @CsvBindByName(column = "LATEST_TIME")
  @CsvDate(DateUtils.SHORT_TIME_PATTERN)
  private Date latestTime;

  @CsvBindByName(column = "DELIVERY_DURATION", required = true)
  private String deliveryDuration;

  @CsvBindByName(column = "CREATED_BY")
  private Long createdUserId;

  @CsvBindByName(column = "MODIFIED_BY")
  private Long modifiedUserId;

  @CsvBindByName(column = "DELIVERY_PROFILE_NAME", required = true)
  private String deliveryProfileName;

}
