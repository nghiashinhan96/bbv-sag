package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.enums.WeekDay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WssBranchDto implements Serializable {

  private static final long serialVersionUID = -8523612141186840957L;

  private Integer id;

  private Integer branchNr;

  private String branchCode;

  private Integer orgId;

  @Valid
  private List<WssBranchOpeningTimeDto> wssBranchOpeningTimes;

  @JsonIgnore
  public List<String> getWssBranchOpeningWeekDays() {
    if (CollectionUtils.isEmpty(wssBranchOpeningTimes)) {
      return Lists.emptyList();
    }
    return wssBranchOpeningTimes.parallelStream()
        .map(wssBranchOpeningTimeDto -> wssBranchOpeningTimeDto.getWeekDay().name())
        .collect(Collectors.toList());
  }

  @JsonIgnore
  public Optional<WssBranchOpeningTimeDto> getWssBranchOpeningTimeOnWeekDay(WeekDay weekDay) {
    if (CollectionUtils.isEmpty(wssBranchOpeningTimes)) {
      return Optional.empty();
    }
    return wssBranchOpeningTimes.parallelStream()
        .filter(wssBranchOpeningTimeDto -> wssBranchOpeningTimeDto.getWeekDay() == weekDay)
        .findFirst();
  }
}
