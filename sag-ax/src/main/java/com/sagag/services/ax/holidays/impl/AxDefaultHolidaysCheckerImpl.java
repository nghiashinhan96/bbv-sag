package com.sagag.services.ax.holidays.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.eshop.repo.api.OpeningDaysCalendarRepository;
import com.sagag.eshop.repo.entity.OpeningDaysCalendar;
import com.sagag.services.ax.holidays.AxHolidaysChecker;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.WorkingDayCode;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysAvailabilityInfo;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysExceptionDto;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysExceptionInfo;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation class for Austria holidays checker.
 *
 */
@Component
@Slf4j
@AxProfile
public class AxDefaultHolidaysCheckerImpl implements AxHolidaysChecker {

  @Autowired
  private OpeningDaysCalendarRepository openingDaysRepo;

  @Override
  public boolean isNonWorkingDay(final OpeningDaysAvailabilityInfo availabilityInfo) {
    log.debug("Check request date is holiday date with {}", availabilityInfo);
    if (availabilityInfo == null) {
      throw new ValidationException("No availability information");
    }
    final OpeningDaysCalendar openingDay = openingDaysRepo
        .findByDateAndCountryName(java.sql.Date.valueOf(localDateOf(availabilityInfo.getDate())),
            availabilityInfo.getCountryName())
        .orElseThrow(() -> new ValidationException("No opening day information"));

    return calculateNonWorkingDay(openingDaysInfoConverter().apply(openingDay), availabilityInfo);
  }

  private Function<OpeningDaysCalendar, OpeningDaysInfo> openingDaysInfoConverter() {
    return entity -> {
      final OpeningDaysExceptionDto exceptionsDto = !StringUtils.isBlank(entity.getExceptions())
          ? SagJSONUtil.convertJsonToObject(entity.getExceptions(), OpeningDaysExceptionDto.class)
          : null;

      final OpeningDaysExceptionInfo exceptions = Objects.isNull(exceptionsDto) ? null
          : OpeningDaysExceptionInfo.builder()
              .affiliate(getSupportedAffiliate(exceptionsDto.getAffiliate()))
              .code(WorkingDayCode.valueOf(exceptionsDto.getWorkingDayCode()))
              .branchNrs(exceptionsDto.getBranches())
              .deliveryAdrressIDs(exceptionsDto.getDeliveryAdrressIDs())
              .build();

      return OpeningDaysInfo.builder()
          .workingDayCode(WorkingDayCode.valueOf(entity.getWorkingDay().getCode()))
          .exceptions(exceptions)
          .build();
    };
  }

  private SupportedAffiliate getSupportedAffiliate(String affiliate) {
    return !StringUtils.isBlank(affiliate) ? SupportedAffiliate.fromCompanyName(affiliate) : null;
  }

  private boolean calculateNonWorkingDay(final OpeningDaysInfo openingDay,
      final OpeningDaysAvailabilityInfo availabilityInfo) {

    final boolean isNonWorkingDay = openingDay.getWorkingDayCode().isNonWorkingDay();
    if (Objects.isNull(openingDay.getExceptions())) {
      return isNonWorkingDay;
    }
    final OpeningDaysExceptionInfo exceptions = openingDay.getExceptions();

    // If no affiliate, then Working Day Code applies to all affiliates for that location
    if (!Objects.isNull(exceptions.getAffiliate())
        && !availabilityInfo.getAffiliate().equals(exceptions.getAffiliate())) {
      return isNonWorkingDay;
    }

    if (exceptions.getBranchNrs().stream()
        .anyMatch(s -> s.equals(availabilityInfo.getPickupBranchId()))) {
      return exceptions.getCode().isNonWorkingDay();
    }
    return isNonWorkingDay;
  }

  private static LocalDate localDateOf(final Date date) {
    return LocalDateTime.ofInstant(date.toInstant(),
        ZoneId.systemDefault()).toLocalDate();
  }
}
