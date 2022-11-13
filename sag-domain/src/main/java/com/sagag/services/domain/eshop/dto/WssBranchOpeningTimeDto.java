package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.domain.eshop.branch.dto.BranchTimeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalTime;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WssBranchOpeningTimeDto implements Serializable {

  private static final long serialVersionUID = 5335940469312913651L;

  private Integer id;

  private Integer wssBranchId;

  @NotNull(message = "Opening time must not be null")
  private String openingTime;

  @NotNull(message = "Closing time must not be null")
  private String closingTime;

  private String lunchStartTime;

  private String lunchEndTime;

  @NotNull
  private WeekDay weekDay;

  @JsonIgnore
  public BranchTimeDto getBranchTimeDto() {

    return BranchTimeDto.builder().openingTime(LocalTime.parse(openingTime))
        .closingTime(LocalTime.parse(closingTime))
        .lunchStartTime(!isNullLunchTime() ? LocalTime.parse(lunchStartTime) : null)
        .lunchEndTime(!isNullLunchTime() ? LocalTime.parse(lunchEndTime) : null).build();
  }

  @JsonIgnore
  public boolean isNullLunchTime() {
    return StringUtils.isBlank(lunchStartTime) || StringUtils.isBlank(lunchEndTime);
  }

}
