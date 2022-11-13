package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.repo.entity.WssOpeningDaysCalendar;
import com.sagag.eshop.repo.entity.WssWorkingDay;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.WssCsvOpeningCalendar;
import com.sagag.services.domain.eshop.dto.WssOpeningDaysDetailDto;
import com.sagag.services.domain.eshop.dto.WssOpeningDaysDto;
import com.sagag.services.domain.eshop.openingdays.dto.WssOpeningDaysExceptionDto;
import com.sagag.services.domain.eshop.openingdays.dto.WssOpeningDaysRequestBody;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Converter for WSS opening days calendar
 */
@UtilityClass
public class WssOpeningDaysConverters {

  public static WssOpeningDaysDetailDto convertToWssOpeningDaysDetail(
      final WssOpeningDaysCalendar openingDaysCalendar) {
    return WssOpeningDaysDetailDto.builder().id(openingDaysCalendar.getId())
        .date(openingDaysCalendar.getDatetime()).countryId(openingDaysCalendar.getCountry().getId())
        .workingDayCode(openingDaysCalendar.getWssWorkingDay().getCode())
        .exceptions(
            StringUtils.isNotBlank(openingDaysCalendar.getExceptions())
                ? SagJSONUtil.convertJsonToObject(
                    openingDaysCalendar.getExceptions(), WssOpeningDaysExceptionDto.class)
                : null)
        .build();
  }

  public static Function<WssOpeningDaysCalendar,
    WssOpeningDaysDetailDto> optionalWssOpeningDaysDetailConverter() {
    return WssOpeningDaysConverters::convertToWssOpeningDaysDetail;
  }

  public static Function<WssOpeningDaysCalendar, WssOpeningDaysDto> wssOpeningDaysDtoConverter() {
    return request -> {
      final WssOpeningDaysExceptionDto exceptions =
          StringUtils.isBlank(request.getExceptions()) ? new WssOpeningDaysExceptionDto()
              : SagJSONUtil.convertJsonToObject(request.getExceptions(),
                  WssOpeningDaysExceptionDto.class);
      return WssOpeningDaysDto.builder().id(request.getId()).date(request.getDatetime())
          .countryName(request.getCountry().getShortName())
          .workingDayCode(request.getWssWorkingDay().getCode())
          .expBranchInfo(
              !CollectionUtils.isEmpty(exceptions.getBranches()) ? exceptions.getBranches().stream()
                  .collect(Collectors.joining(SagConstants.COMMA_NO_SPACE)) : StringUtils.EMPTY)
          .expWorkingDayCode(exceptions.getWorkingDayCode()).build();
    };
  }

  public static WssOpeningDaysCalendar wssOpeningDaysEntityConverter(
      final WssOpeningDaysRequestBody request, final Country country,
      final WssWorkingDay wssWorkingDay) {
    return WssOpeningDaysCalendar.builder().id(request.getId())
        .datetime(Date.valueOf(request.getDate())).country(country).wssWorkingDay(wssWorkingDay)
        .exceptions(!Objects.isNull(request.getExceptions())
            ? SagJSONUtil.convertObjectToJson(request.getExceptions())
            : null)
        .build();
  }

  public static WssOpeningDaysCalendar wssOpeningDaysEntityConverter(
      final WssCsvOpeningCalendar request, final Country country, final WssWorkingDay wssWorkingDay,
      int orgId) {
    return WssOpeningDaysCalendar.builder().id(request.getId()).datetime(request.getDate())
        .country(country).wssWorkingDay(wssWorkingDay).orgId(orgId).build();
  }
}
