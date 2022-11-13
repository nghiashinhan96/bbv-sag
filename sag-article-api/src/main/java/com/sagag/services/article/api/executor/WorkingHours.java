package com.sagag.services.article.api.executor;

import com.sagag.services.common.enums.WeekDay;

import lombok.Builder;
import lombok.Data;

import org.joda.time.LocalTime;

@Data
@Builder
public class WorkingHours {
  private LocalTime openingTime;
  private LocalTime closingTime;
  private LocalTime lunchStartTime;
  private LocalTime lunchEndTime;
  private WeekDay weekDay;
}
