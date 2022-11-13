package com.sagag.services.ax.availability.calculator;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.ax.availability.tourtime.impl.AxDefaultTourTimeTableGenerator;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.TourTimeTable;

import lombok.extern.slf4j.Slf4j;

@Component
@AxProfile
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class ExternalSourceTourArrivalTimeCalculator implements ArrivalTimeCalculator {

  private static final int LENGTH_OF_DEPARTURE_TIME = 2;

  private static final int ONE_DAY_FOR_TOUR_DAYS = 1;

  @Autowired
  private AxDefaultTourTimeTableGenerator axDefTourTimeTableGenerator;

  @Override
  @LogExecutionTime
  public boolean calculateArrivalTime(Availability availability,
      Optional<Date> nextWorkingDateForToday, SupportedAffiliate affiliate, String pickupBranchId,
      List<TourTimeDto> tourTimeList, List<WorkingHours> openingHours, String countryName) {
    if (availability.isExternalSource()) {
      findTourTimeGoodEnoughWithArrivalTime(availability.getUTCArrivalTime(),
          tourTimeList, affiliate, pickupBranchId)
      .ifPresent(tourTime -> {
        availability.setTourName(tourTime.getTourName());
        availability.setArrivalTime(tourTime.getStartTime());
      });
    }
    return true;
  }

  private Optional<TourTimeTable> findTourTimeGoodEnoughWithArrivalTime(DateTime utcDateTime,
      List<TourTimeDto> tourTimeList, SupportedAffiliate affiliate, String branchId) {
    if (utcDateTime == null) {
      return Optional.empty();
    }
    final DateTime arrivalTimeWithCET = new DateTime(utcDateTime,
        DateTimeZone.forID(DateUtils.TZ_CET_STR));
    final List<TourTimeTable> tours = axDefTourTimeTableGenerator
        .generate(arrivalTimeWithCET, tourTimeList, branchId, affiliate, StringUtils.EMPTY);

    // Get first tour time table if it's valid with arrival time
    final Optional<TourTimeTable> tourBeforeArrivalTime = tours.stream()
        .filter(item -> checkArrivalTimeValidWithCurrentTour(arrivalTimeWithCET, item))
        .findFirst();

    if (tourBeforeArrivalTime.isPresent()) {
      log.info("Valid Tour Time for current date time: {} - tour time: {}",
          arrivalTimeWithCET, SagJSONUtil.convertObjectToPrettyJson(tourBeforeArrivalTime.get()));
      return tourBeforeArrivalTime;
    }

    final Date nextWorkingDate;
    // Find tours next work day of week of branch id
    final DateTime nextWorkingDateTime = findNextWorkingDateTimeWithTourTime(tourTimeList,
        arrivalTimeWithCET.plusDays(ONE_DAY_FOR_TOUR_DAYS));
    nextWorkingDate = nextWorkingDateTime.toDate();

    if (nextWorkingDate == null) {
      log.info("Not found any next working date from ERP with request date = {}, "
          + "so keep original value of arrival time", arrivalTimeWithCET);
      return Optional.empty();
    }

    log.warn("Not found any tour time with date time: {} - next working date: {}",
        arrivalTimeWithCET, nextWorkingDate);

    final List<TourTimeTable> toursForNextWorkingDate = axDefTourTimeTableGenerator
        .generate(new DateTime(nextWorkingDate), tourTimeList, branchId, affiliate,
            StringUtils.EMPTY);

    return toursForNextWorkingDate.stream().findFirst();
  }

  private static boolean checkArrivalTimeValidWithCurrentTour(DateTime arrivalTimeWithCET,
      TourTimeTable tourTimeTable) {
    Assert.notNull(tourTimeTable, "The given tour time table must not be null");

    final int cutOffMinutes = Optional.ofNullable(tourTimeTable.getCutOffMinutes()).orElse(0);
    final String departureTime = tourTimeTable.getTourDepartureTime();
    final String tourDays = tourTimeTable.getTourDays();

    if (StringUtils.isAnyBlank(departureTime, tourDays)) {
      return arrivalTimeWithCET.isBefore(tourTimeTable.getCETStartTime());
    }

    final int dayOfWeekOfArrivalTime = arrivalTimeWithCET.getDayOfWeek();
    final List<Integer> dayOfWeeksOfTourDays = getDayOfWeekFromTourDays(tourDays);
    if (!dayOfWeeksOfTourDays.contains(dayOfWeekOfArrivalTime)) {
      return false;
    }

    final String[] departureTimeArr = StringUtils.split(departureTime, SagConstants.COLON);
    if (ArrayUtils.getLength(departureTimeArr) != LENGTH_OF_DEPARTURE_TIME) {
      return false;
    }
    final String hourOfDepartureTime = departureTimeArr[0];
    final String minuteOfDepartureTime = departureTimeArr[1];
    final DateTime departureDateTime = new DateTime(arrivalTimeWithCET)
        .withHourOfDay(NumberUtils.toInt(hourOfDepartureTime))
        .withMinuteOfHour(NumberUtils.toInt(minuteOfDepartureTime));

    final DateTime validTourDateTime = departureDateTime.minusMinutes(cutOffMinutes);
    return !arrivalTimeWithCET.isAfter(validTourDateTime);
  }

  private static DateTime findNextWorkingDateTimeWithTourTime(List<TourTimeDto> tourTimeList,
      DateTime arrivalTimeWithCET) {
    final int dayOfWeekOfArrivalTime = arrivalTimeWithCET.getDayOfWeek();

    final Optional<TourTimeDto> firstTourTimeGoodWithNextTour = tourTimeList.stream()
    .filter(tourTime -> getDayOfWeekFromTourDays(tourTime.getTourDays()).stream().anyMatch(
        dayOfWeek -> NumberUtils.compare(dayOfWeek, dayOfWeekOfArrivalTime) >= 0))
    .findFirst();

    if (!firstTourTimeGoodWithNextTour.isPresent()) {
      final Optional<TourTimeDto> lastTourTimeGoodWithNextTour = tourTimeList.stream()
          .filter(tourTime -> getDayOfWeekFromTourDays(tourTime.getTourDays()).stream().anyMatch(
              dayOfWeek -> NumberUtils.compare(dayOfWeek, dayOfWeekOfArrivalTime) < 0))
          .findFirst();

      return findNextWorkingDateTimeWithTourTime(
          lastTourTimeGoodWithNextTour.orElse(null), arrivalTimeWithCET);
    }

    return findNextWorkingDateTimeWithTourTime(
        firstTourTimeGoodWithNextTour.orElse(null), arrivalTimeWithCET);
  }

  private static DateTime findNextWorkingDateTimeWithTourTime(TourTimeDto tourTime,
      DateTime arrivalTimeWithCET) {
    if (tourTime == null) {
      return arrivalTimeWithCET;
    }
    final List<Integer> dayOfWeeksOfTourDaysWithNextTour =
        getDayOfWeekFromTourDays(tourTime.getTourDays());

    final List<DateTime> potentialNextTourList = new LinkedList<>();
    potentialNextTourList.add(arrivalTimeWithCET);

    IntStream.range(DateTimeConstants.MONDAY, DateTimeConstants.SUNDAY)
        .boxed().map(arrivalTimeWithCET::plusDays).forEach(potentialNextTourList::add);

    final Optional<DateTime> potentialNextTour = potentialNextTourList.stream()
        .filter(tourDateTime -> dayOfWeeksOfTourDaysWithNextTour.contains(
            tourDateTime.getDayOfWeek())).findFirst();

    return potentialNextTour.orElse(arrivalTimeWithCET);
  }

  private static List<Integer> getDayOfWeekFromTourDays(String tourDays) {
    if (StringUtils.isBlank(tourDays)) {
      return Collections.emptyList();
    }
    return Stream.of(ArrayUtils
        .nullToEmpty(StringUtils.split(tourDays, SagConstants.COMMA_NO_SPACE)))
        .map(StringUtils::trimToEmpty).filter(StringUtils::isNotBlank)
        .map(NumberUtils::createInteger).filter(Objects::nonNull)
        .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
  }

  @Override
  public ErpSendMethodEnum sendMethodMode() {
    return ErpSendMethodEnum.TOUR;
  }

}
