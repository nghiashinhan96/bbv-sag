package com.sagag.services.ax.availability.tourtime.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.ax.availability.backorder.BackOrderUtils;
import com.sagag.services.ax.availability.tourtime.NextWorkingDateHelper;
import com.sagag.services.ax.availability.tourtime.TourTimeTableGenerator;
import com.sagag.services.ax.holidays.impl.AxDefaultHolidaysCheckerImpl;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysAvailabilityInfo;
import com.sagag.services.domain.sag.erp.TourTimeTable;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation class for tour time table generator.
 *
 */
@Component
@Slf4j
@AxProfile
public class AxBackOrderTourTimeTableGenerator implements TourTimeTableGenerator {

  private static final int HOUR_OF_DAY = 24;

  @Autowired
  private AxDefaultHolidaysCheckerImpl axAustriaHolidaysChecker;

  @Autowired
  private AxDefaultTourTimeTableGenerator axDefTourTimeTableGenerator;

  @Autowired
  private NextWorkingDateHelper nextWrkDateHelper;

  @Override
  @LogExecutionTime
  public List<TourTimeTable> generate(final DateTime nextWorkingDate,
      final List<TourTimeDto> tourTimeList, final String branchId,
      final SupportedAffiliate affiliate, String countryName) {
    log.debug("Start generate tour time table for availability object.");

    if (Objects.isNull(nextWorkingDate) || CollectionUtils.isEmpty(tourTimeList)) {
      return Collections.emptyList();
    }

    // Generate tour time table list for next working date
    final List<TourTimeTable> tourTimeTableListOfNextWorkingDate = axDefTourTimeTableGenerator
        .generate(nextWorkingDate, tourTimeList, branchId, affiliate, countryName);


    // Generate tour time table list for current date
    // #4030: Set the availability to nextWorkingDate (now() +144 hours) , which is >= 6 days from today
    int backOrderDays = BackOrderUtils.getBackOrderDays(affiliate);
    final DateTime requestDate = new DateTime(DateUtils.getDateLaterFromCurrent(backOrderDays));
    final OpeningDaysAvailabilityInfo daysAvaiInfo = OpeningDaysAvailabilityInfo.builder()
        .affiliate(affiliate).pickupBranchId(branchId).date(requestDate.toDate())
        .countryName(countryName).build();
    if (isFetchNextWorkingDate(daysAvaiInfo)) {
      return tourTimeTableListOfNextWorkingDate;
    }
    final List<TourTimeTable> tourTimeTableList = axDefTourTimeTableGenerator
        .generate(requestDate, tourTimeList, branchId, affiliate, countryName);

    if (CollectionUtils.isEmpty(tourTimeTableListOfNextWorkingDate)) {
      return tourTimeTableList;
    }

    final List<TourTimeTable> nextWorkingDateTourTimeTableList =
        getTourTimeTableNextWorkingDateWithFullHour(requestDate, nextWorkingDate,
            tourTimeTableListOfNextWorkingDate);
    if (!isAfterLastTour(findLastTourDateTime(tourTimeTableList), requestDate)) {
      return ListUtils.union(tourTimeTableList, nextWorkingDateTourTimeTableList);
    }

    // Get next of next working date
    final Optional<Date> nextOfNextWorkingDateOpt = Optional.ofNullable(
        nextWrkDateHelper.getNextWorkingDate(affiliate.getCompanyName(), branchId,
            nextWorkingDate.toDate()));

    if (!nextOfNextWorkingDateOpt.isPresent()) {
      return ListUtils.union(tourTimeTableList, nextWorkingDateTourTimeTableList);
    }

    // Calculate the request time is after last tour
    final DateTime nextOfNextWorkingDateTime = new DateTime(nextOfNextWorkingDateOpt.get());
    final List<TourTimeTable> tourTimeTableListOfNextOfNextWorkingDate = axDefTourTimeTableGenerator
        .generate(nextOfNextWorkingDateTime, tourTimeList, branchId, affiliate, countryName);

    final List<TourTimeTable> filteredNextOfNextWorkingDateTourTimeTableList =
        getTourTimeTableNextWorkingDateWithFullHour(nextWorkingDate,
        nextOfNextWorkingDateTime, tourTimeTableListOfNextOfNextWorkingDate);

    if (!CollectionUtils.isEmpty(filteredNextOfNextWorkingDateTourTimeTableList)) {
      nextWorkingDateTourTimeTableList.add(filteredNextOfNextWorkingDateTourTimeTableList.get(0));
    }

    return nextWorkingDateTourTimeTableList;
  }

  private boolean isFetchNextWorkingDate(final OpeningDaysAvailabilityInfo daysAvaiInfo) {
    return axAustriaHolidaysChecker.isNonWorkingDay(daysAvaiInfo);
  }

  private static List<TourTimeTable> getTourTimeTableNextWorkingDateWithFullHour(
      final DateTime requestDateTime, final DateTime nextWorkingDate,
      final List<TourTimeTable> tourTimeTableListOfNextWorkingDate) {

    final List<TourTimeTable> tourTimeTableList = new ArrayList<>();
    final int differDays = Days.daysBetween(requestDateTime, nextWorkingDate).getDays();
    final DateTime dateTime = requestDateTime.plusHours(HOUR_OF_DAY * (differDays + 1));
    DateTime startDateTime;
    for (TourTimeTable tourTimeTable : tourTimeTableListOfNextWorkingDate) {
      startDateTime = tourTimeTable.getCETStartTime();
      if (startDateTime.isEqual(dateTime) || startDateTime.isAfter(dateTime)) {
        tourTimeTableList.add(tourTimeTable);
        break;
      }
      if (startDateTime.isBefore(dateTime)) {
        tourTimeTableList.add(tourTimeTable);
      }
    }
    return tourTimeTableList;
  }

  private static boolean isAfterLastTour(final DateTime lastTourDateTime,
    final DateTime requestDateTime) {
    return requestDateTime.isAfter(lastTourDateTime);
  }

  private static DateTime findLastTourDateTime(final List<TourTimeTable> tourTimeTableList) {
    return Iterables.getLast(tourTimeTableList).getCETStartTime();
  }
}
