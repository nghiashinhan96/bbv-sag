package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.WssTourTimes;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.dto.WssTourTimesDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Converter for WSS Tour Times
 */
@UtilityClass
public final class WssTourTimesConverters {

  public static WssTourTimesDto convertToWssTourTimesDto(final WssTourTimes tourTimes) {
    return WssTourTimesDto.builder().id(tourTimes.getId()).weekDay(tourTimes.getWeekDay())
        .departureTime(
            DateUtils.toStringDate(tourTimes.getDepartureTime(), DateUtils.SHORT_TIME_PATTERN))
        .build();
  }

  public static List<WssTourTimesDto> convertWssTourTimesToDto(List<WssTourTimes> wssTourTimes) {
    if (CollectionUtils.isEmpty(wssTourTimes)) {
      return new ArrayList<>();
    }
    return wssTourTimes.stream().map(WssTourTimesConverters::convertToWssTourTimesDto)
        .collect(Collectors.toList());
  }

  public static WssTourTimes convertToWssTourTimesFromDto(final WssTourTimesDto tourTimes) {
    return WssTourTimes.builder()
        .departureTime(
            DateUtils.convertStringToTime(tourTimes.getDepartureTime(), DateUtils.TIME_PATTERN))
        .weekDay(tourTimes.getWeekDay()).build();
  }

  public static List<WssTourTimes> convertWssTourTimesFromDtos(
      List<WssTourTimesDto> wssTourTimesDtos) {
    if (CollectionUtils.isEmpty(wssTourTimesDtos)) {
      return new ArrayList<>();
    }
    return wssTourTimesDtos.stream()
        .filter(tourTime -> StringUtils.isNotBlank(tourTime.getDepartureTime()))
        .map(WssTourTimesConverters::convertToWssTourTimesFromDto).collect(Collectors.toList());
  }

  public static Function<WssTourTimes, WssTourTimesDto> optionalTourConverter() {
    return WssTourTimesConverters::convertToWssTourTimesDto;
  }

}
