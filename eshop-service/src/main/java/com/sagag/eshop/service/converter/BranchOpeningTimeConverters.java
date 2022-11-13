package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.BranchOpeningTime;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.branch.dto.BranchTimeDto;
import com.sagag.services.domain.eshop.dto.BranchOpeningTimeDto;

import lombok.experimental.UtilityClass;

import java.sql.Time;
import java.util.Objects;

/**
 * Converter for branch opening time
 */
@UtilityClass
public final class BranchOpeningTimeConverters {

  public static BranchOpeningTimeDto convertToBranchOpeningTimeDto(
      final BranchOpeningTime branchOpeningTime) {
    return BranchOpeningTimeDto.builder().id(branchOpeningTime.getId())
        .branchId(branchOpeningTime.getBranchId())
        .openingTime(DateUtils.toStringDate(branchOpeningTime.getOpeningTime(),
            DateUtils.SHORT_TIME_PATTERN))
        .closingTime(DateUtils.toStringDate(branchOpeningTime.getClosingTime(),
            DateUtils.SHORT_TIME_PATTERN))
        .lunchStartTime(
            !Objects.isNull(branchOpeningTime.getLunchStartTime()) ? DateUtils.toStringDate(
                branchOpeningTime.getLunchStartTime(), DateUtils.SHORT_TIME_PATTERN) : null)
        .lunchEndTime(!Objects.isNull(branchOpeningTime.getLunchEndTime()) ? DateUtils
            .toStringDate(branchOpeningTime.getLunchEndTime(), DateUtils.SHORT_TIME_PATTERN) : null)
        .weekDay(branchOpeningTime.getWeekDay()).build();
  }

  public static BranchOpeningTime convertFromDto(final BranchOpeningTimeDto branchOpeningTimeDto) {
    final BranchTimeDto branchTimeDto = branchOpeningTimeDto.getBranchTimeDto();
    return BranchOpeningTime.builder().weekDay(branchOpeningTimeDto.getWeekDay())
        .openingTime(Time.valueOf(branchTimeDto.getOpeningTime().toString(DateUtils.TIME_PATTERN)))
        .closingTime(Time.valueOf(branchTimeDto.getClosingTime().toString(DateUtils.TIME_PATTERN)))
        .openingTime(Time.valueOf(branchTimeDto.getOpeningTime().toString(DateUtils.TIME_PATTERN)))
        .closingTime(Time.valueOf(branchTimeDto.getClosingTime().toString(DateUtils.TIME_PATTERN)))
        .lunchStartTime(!branchOpeningTimeDto.isNullLunchTime()
            ? Time.valueOf(branchTimeDto.getLunchStartTime().toString(DateUtils.TIME_PATTERN))
            : null)
        .lunchEndTime(!branchOpeningTimeDto.isNullLunchTime()
            ? Time.valueOf(branchTimeDto.getLunchEndTime().toString(DateUtils.TIME_PATTERN))
            : null)
        .build();
  }

}
