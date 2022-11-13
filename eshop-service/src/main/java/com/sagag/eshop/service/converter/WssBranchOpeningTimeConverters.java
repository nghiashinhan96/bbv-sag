package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.WssBranchOpeningTime;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.branch.dto.BranchTimeDto;
import com.sagag.services.domain.eshop.dto.WssBranchOpeningTimeDto;

import lombok.experimental.UtilityClass;

import java.sql.Time;
import java.util.Objects;

/**
 * Converter for WSS branch
 */
@UtilityClass
public final class WssBranchOpeningTimeConverters {

  public static WssBranchOpeningTimeDto convertToWssBranchOpeningTimeDto(
      final WssBranchOpeningTime wssBranchOpeningTime) {
    return WssBranchOpeningTimeDto.builder().id(wssBranchOpeningTime.getId())
        .wssBranchId(wssBranchOpeningTime.getWssBranch().getId())
        .openingTime(DateUtils.toStringDate(wssBranchOpeningTime.getOpeningTime(),
            DateUtils.SHORT_TIME_PATTERN))
        .closingTime(DateUtils.toStringDate(wssBranchOpeningTime.getClosingTime(),
            DateUtils.SHORT_TIME_PATTERN))
        .lunchStartTime(
            !Objects.isNull(wssBranchOpeningTime.getLunchStartTime()) ? DateUtils.toStringDate(
                wssBranchOpeningTime.getLunchStartTime(), DateUtils.SHORT_TIME_PATTERN) : null)
        .lunchEndTime(!Objects.isNull(wssBranchOpeningTime.getLunchEndTime()) ? DateUtils
            .toStringDate(wssBranchOpeningTime.getLunchEndTime(), DateUtils.SHORT_TIME_PATTERN)
            : null)
        .weekDay(wssBranchOpeningTime.getWeekDay()).build();
  }

  public static WssBranchOpeningTime convertFromDto(
      final WssBranchOpeningTimeDto wssBranchOpeningTimeDto) {
    final BranchTimeDto wssBranchTimeDto = wssBranchOpeningTimeDto.getBranchTimeDto();
    return WssBranchOpeningTime.builder().weekDay(wssBranchOpeningTimeDto.getWeekDay())
        .openingTime(
            Time.valueOf(wssBranchTimeDto.getOpeningTime().toString(DateUtils.TIME_PATTERN)))
        .closingTime(
            Time.valueOf(wssBranchTimeDto.getClosingTime().toString(DateUtils.TIME_PATTERN)))
        .openingTime(
            Time.valueOf(wssBranchTimeDto.getOpeningTime().toString(DateUtils.TIME_PATTERN)))
        .closingTime(
            Time.valueOf(wssBranchTimeDto.getClosingTime().toString(DateUtils.TIME_PATTERN)))
        .lunchStartTime(!wssBranchOpeningTimeDto.isNullLunchTime()
            ? Time.valueOf(wssBranchTimeDto.getLunchStartTime().toString(DateUtils.TIME_PATTERN))
            : null)
        .lunchEndTime(!wssBranchOpeningTimeDto.isNullLunchTime()
            ? Time.valueOf(wssBranchTimeDto.getLunchEndTime().toString(DateUtils.TIME_PATTERN))
            : null)
        .build();
  }

}
