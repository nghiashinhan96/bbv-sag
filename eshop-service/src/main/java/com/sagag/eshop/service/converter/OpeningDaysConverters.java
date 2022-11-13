package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.repo.entity.OpeningDaysCalendar;
import com.sagag.eshop.repo.entity.WorkingDay;
import com.sagag.eshop.service.dto.CsvOpeningCalendar;
import com.sagag.eshop.service.dto.OpeningDaysDetailDto;
import com.sagag.eshop.service.dto.OpeningDaysDto;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysExceptionDto;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysRequestBody;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Converter for opening days calendar
 */
@UtilityClass
public final class OpeningDaysConverters {

  public static OpeningDaysDetailDto convertToOpeningDaysDetail(final OpeningDaysCalendar entity) {
    return new OpeningDaysDetailDto(entity);
  }

  public static Function<OpeningDaysCalendar, OpeningDaysDetailDto> optionalOpeningDaysDetailConverter() {
    return OpeningDaysConverters::convertToOpeningDaysDetail;
  }
  
  public static Function<OpeningDaysCalendar, OpeningDaysDto> openingDaysDtoConverter() {
    return request -> {
      final OpeningDaysExceptionDto exceptions =
          StringUtils.isBlank(request.getExceptions()) ? new OpeningDaysExceptionDto()
              : SagJSONUtil.convertJsonToObject(request.getExceptions(),
                  OpeningDaysExceptionDto.class);
      return OpeningDaysDto.builder().id(request.getId()).date(request.getDatetime())
          .countryName(request.getCountry().getShortName())
          .workingDayCode(request.getWorkingDay().getCode())
          .expAffiliate(exceptions.getAffiliate())
          .expBranchInfo(
              !CollectionUtils.isEmpty(exceptions.getBranches()) ? exceptions.getBranches().stream()
                  .collect(Collectors.joining(SagConstants.COMMA_NO_SPACE)) : StringUtils.EMPTY)
          .expWorkingDayCode(exceptions.getWorkingDayCode())
          .expDeliveryAddressId(!CollectionUtils.isEmpty(exceptions.getDeliveryAdrressIDs())
              ? exceptions.getDeliveryAdrressIDs().stream()
                  .collect(Collectors.joining(SagConstants.COMMA_NO_SPACE))
              : StringUtils.EMPTY)
          .build();
    };
  }

  public static OpeningDaysCalendar openingDaysEntityConverter(final OpeningDaysRequestBody request,
      final Country country, final WorkingDay workingDay) {
      return OpeningDaysCalendar.builder()
          .id(request.getId())
        .datetime(Date.valueOf(request.getDate()))
        .country(country)
        .workingDay(workingDay)
        .exceptions(!Objects.isNull(request.getExceptions())
            ? SagJSONUtil.convertObjectToJson(request.getExceptions())
            : null)
        .build();
  }
  
  public static OpeningDaysCalendar openingDaysEntityConverter(final CsvOpeningCalendar request,
      final Country country, final WorkingDay workingDay) {
      return OpeningDaysCalendar.builder()
          .id(request.getId())
          .datetime(request.getDate())
          .country(country)
          .workingDay(workingDay)
          .build();
  }
}
