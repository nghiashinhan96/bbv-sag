package com.sagag.services.ax.availability.backorder;

import static com.sagag.services.common.enums.ErpSendMethodEnum.TOUR;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.IArticleAvailabilityProcessor;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.ax.availability.filter.AvailabilityCriteria;
import com.sagag.services.ax.availability.tourtime.impl.AxBackOrderTourTimeTableGenerator;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.TourTimeTable;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AxProfile
public class TourTimeTableBackOrderAvailabilityHandler implements IBackOrderAvailablityHandler {

  @Autowired
  private IArticleAvailabilityProcessor availabilityProcessor;

  @Autowired
  private AxBackOrderTourTimeTableGenerator axTourTimeTableGenerator;

  @Override
  public boolean handle(Availability availability, Object... objects) {
    final AvailabilityCriteria availabilityCriteria = (AvailabilityCriteria) objects[0];
    final ErpSendMethodEnum sendMethodEnum = availabilityCriteria.getSendMethodEnum();
    final List<TourTimeDto> tourTimeList = availabilityCriteria.getTourTimeList();
    final String pickupBranchId = availabilityCriteria.getPickupBranchId();
    final SupportedAffiliate affiliate = availabilityCriteria.getAffiliate();
    final Date nextWorkingDate = availabilityCriteria.getNextWorkingDate();
    final DateTime lastTourTime = availabilityCriteria.getLastTourTime();
    final List<WorkingHours> openingHours = availabilityCriteria.getOpeningHours();
    // Process next working tour when send method is TOUR
    if (isValidToFetchNewAvailabilityWithBackOrderCase(sendMethodEnum, availability)) {

      final List<TourTimeTable> tourTimeTable =
          axTourTimeTableGenerator.generate(new DateTime(nextWorkingDate.getTime()), tourTimeList,
              pickupBranchId, affiliate, availabilityCriteria.getCountryName());
      availability.setTourTimeTable(tourTimeTable);
    }
    log.debug("Newest avail = {}", availability);
    log.debug("Fetch the next tour time lying immediately after the time in default time from");

    final TourTimeTable nextWorkingTour =
        getNextWorkingTour(availability, lastTourTime, sendMethodEnum, openingHours);

    log.info("Next working tour = {}", nextWorkingTour);
    // handle isTourTableTable true but no tour time table existed
    if (Objects.isNull(nextWorkingTour)) {
      ArticleAvailabilityResult extendedResult = availabilityProcessor.getDefaultResult();
      availability.setAvailState(extendedResult.getAvailablityStateCode());
      availability.setAvailStateColor(extendedResult.getAvailablityStateColor());
      return false;
    }

    availability.setTourName(nextWorkingTour.getTourName());
    availability.setArrivalTime(nextWorkingTour.getStartTime());
    return true;
  }

  private static boolean isValidToFetchNewAvailabilityWithBackOrderCase(
      final ErpSendMethodEnum sendMethodEnum, final Availability availability) {
    return TOUR == sendMethodEnum && availability != null && !availability.hasTourTimeTable();
  }

  /**
   * Returns the next working tour.
   *
   */
  private static TourTimeTable getNextWorkingTour(final Availability availability,
      final DateTime lastTourTime, final ErpSendMethodEnum sendMethodEnum,
      final List<WorkingHours> openingHours) {
    if(CollectionUtils.isEmpty(openingHours)) {
       return null;
    }
    final List<WeekDay> workingWeekDays = openingHours.stream().map(WorkingHours::getWeekDay).collect(Collectors.toList());
    final DateTime requestTime = DateTime.now();
    final int days =
        Days.daysBetween(requestTime.toLocalDate(), lastTourTime.toLocalDate()).getDays();
    DateTime nextWorkingDateTime = requestTime.plusDays(days); // default time is next working

    // default time is for PICKUP
    TourTimeTable nextWorkingTour = new TourTimeTable();
    nextWorkingDateTime = findNextWorkingDayOpeningTime(openingHours, workingWeekDays,
        nextWorkingDateTime, nextWorkingTour);
    log.info("Default PICKUP time: {}", nextWorkingTour.getStartTime());

    // Process next working tour when send method is TOUR
    if (TOUR == sendMethodEnum) { // if TOUR then get next tour from default pickup time
      return availability.getZKNextWorkingTour(nextWorkingDateTime);
    }
    return nextWorkingTour;
  }

  private static DateTime findNextWorkingDayOpeningTime(final List<WorkingHours> openingHours,
      final List<WeekDay> workingWeekDays, DateTime nextWorkingDateTime,
      TourTimeTable nextWorkingTour) {
    WeekDay defaultNextWorkingWeekDay = WeekDay.findByValue(nextWorkingDateTime.getDayOfWeek());
    if (!workingWeekDays.contains(defaultNextWorkingWeekDay)) {
      while (!workingWeekDays.contains(defaultNextWorkingWeekDay)) {
        nextWorkingDateTime = nextWorkingDateTime.plusDays(1);
        defaultNextWorkingWeekDay = WeekDay.findByValue(nextWorkingDateTime.getDayOfWeek());
      }
    }
    for (WorkingHours workingDay : openingHours) {
      if (workingDay.getWeekDay() == defaultNextWorkingWeekDay) {
        nextWorkingTour.setStartTime(AxArticleUtils.toUTCArrivalTime(nextWorkingDateTime,
            workingDay.getOpeningTime()));
      }
    }
    return nextWorkingDateTime;
  }
}
