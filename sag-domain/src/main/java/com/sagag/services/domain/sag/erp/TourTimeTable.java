package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.utils.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourTimeTable implements Serializable {

  private static final long serialVersionUID = -8377646423279871110L;

  private String tourName;

  private String startTime;

  private String tourDays;

  private String tourDepartureTime;

  private Integer cutOffMinutes;

  @JsonIgnore
  public DateTime getCETStartTime() { // this code is running on CET server
    return DateUtils.getCETDateTime(startTime); // implicit timezone conversion
  }

}
