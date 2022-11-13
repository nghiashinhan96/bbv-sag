package com.sagag.services.ax.availability.calculator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sagag.eshop.repo.api.OpeningDaysCalendarRepository;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.ax.availability.processor.AxArticleAvailabilityProcessor;
import com.sagag.services.ax.availability.tourtime.NextWorkingDateHelper;
import com.sagag.services.ax.exception.WorkingDayNotFoundException;
import com.sagag.services.ax.holidays.AxHolidaysChecker;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysAvailabilityInfo;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.extern.slf4j.Slf4j;

@Component
@AxProfile
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PickupArrivalTimeCalculator implements ArrivalTimeCalculator {

  @Autowired
  private AxHolidaysChecker axHolidaysChecker;

  @Autowired
  private OpeningDaysCalendarRepository openingDaysRepo;

  @Autowired
  private AxArticleAvailabilityProcessor availabilityProcessor;

  @Autowired
  private NextWorkingDateHelper nextWrkDateHelper;

  @Override
  public ErpSendMethodEnum sendMethodMode() {
    return ErpSendMethodEnum.PICKUP;
  }

  @Override
  public boolean calculateArrivalTime(Availability availability,
      Optional<Date> nextWorkingDateForToday, SupportedAffiliate affiliate, String pickupBranchId,
      List<TourTimeDto> tourTimeList, List<WorkingHours> openingHours, String countryName) {
    if (CollectionUtils.isEmpty(openingHours)) {
      return false;
    }
    ErpSendMethodEnum sendMethodEnum = sendMethodMode();
    boolean isSofort = availabilityProcessor.isDeliveryImmediate(sendMethodEnum,
        new DateTime(availability.getArrivalTime()), availability.isImmediateDelivery());
    availability.setSofort(isSofort);

    log.debug("calculateArrivalTimeForPickup availability = {}", availability);

    // #2869: [AT-AX] Out-of-hours Pick-up time
    final OpeningDaysAvailabilityInfo daysAvailInfo = OpeningDaysAvailabilityInfo.builder()
        .affiliate(affiliate)
        .countryName(countryName)
        .pickupBranchId(pickupBranchId).build();

    try {
      if (isSofort) {
        calculateIfOutsideWorkingHoursSofort(availability, openingHours, daysAvailInfo);
      } else {
        calculateIfOutsideWorkingHoursNonSofort(availability,
            affiliate.getCompanyName(), pickupBranchId, openingHours, daysAvailInfo);
      }
      return true;
    } catch (WorkingDayNotFoundException e) {
      return false;
    } finally {
      log.debug("calculateArrivalTimeForPickup availability updated = {}", availability);
    }
  }

  /**
   * Calculates arrival time for immediate pickup delivery and user request time is out of working hours.
   *
   * @param availability the ax availability
   * @param nextWorkingDate the next working day
   * @param workingHours the working hours of branch
   * @param daysAvailInfo
   * @throws WorkingDayNotFoundException
   */
  private void calculateIfOutsideWorkingHoursSofort(Availability availability,
      List<WorkingHours> workingHours, final OpeningDaysAvailabilityInfo daysAvailInfo)
          throws WorkingDayNotFoundException {
    Assert.notEmpty(workingHours, "Branch working hour is not available");

    final DateTime now = DateTime.now();

    DateTime newArrivalTime;

    final DateTime axArrivalTime = new DateTime(availability.getArrivalTime());

    // #5212: [CH/AT] Calculate pick-up availability based on Calendar and opening times
    // If the availability date is a non-working day then take
    // the next working day from the Connect Opening Day calendar and the opening time
    // from the branch opening times.
    daysAvailInfo.setDate(axArrivalTime.toDate());

    if (axHolidaysChecker.isNonWorkingDay(daysAvailInfo)
        || !isBranchWorkingDay(workingHours, now)) {
      final Date newNextWorkingDate = findInternalNextWorkingDate(axArrivalTime.toDate(),
          daysAvailInfo.getCountryName(), daysAvailInfo.getAffiliate().getCompanyName(),
          daysAvailInfo.getPickupBranchId());
      final Optional<WorkingHours> nextWorkingWeekDay = getWorkingWeekDay(workingHours, newNextWorkingDate);
      if (!nextWorkingWeekDay.isPresent()) {
        throw new WorkingDayNotFoundException("Next working day not found");
      }
      final LocalTime nextOpeningTime = nextWorkingWeekDay.map(WorkingHours::getOpeningTime).orElse(null);
      newArrivalTime = resetToOpenTime(newNextWorkingDate, nextOpeningTime);
      availability.setArrivalTime(
          AxArticleUtils.toUTCArrivalTime(newArrivalTime, nextOpeningTime));
      availability.setSofort(false);
      return;
    }

    final Optional<WorkingHours> workingWeekDay = getWorkingWeekDay(workingHours, now.toDate());
    if (!workingWeekDay.isPresent()) {
      throw new WorkingDayNotFoundException("Next working day from now not found");
    }
    WorkingHours workingHour = workingWeekDay.get();
    DateTime openTime = now.withTime(workingHour.getOpeningTime());
    if (openTime.isAfterNow()) {
      newArrivalTime = resetToOpenTime(now.toDate(), workingHour.getOpeningTime());
      availability.setSofort(false);
      availability.setArrivalTime(
          AxArticleUtils.toUTCArrivalTime(newArrivalTime, workingHour.getOpeningTime()));
      return;
    }

    if (isLunchTime(now.toLocalTime(), workingHour.getLunchStartTime(), workingHour.getLunchEndTime())) {
      availability.setSofort(false);
      newArrivalTime = resetToOpenTime(now.toDate(), workingHour.getLunchEndTime());
      availability.setArrivalTime(
          AxArticleUtils.toUTCArrivalTime(newArrivalTime, workingHour.getOpeningTime()));
    }
  }

  /**
   * Calculates arrival time for specific arrival time (not immediate) in pickup delivery mode
   * and user request time is out of working hours.
   *
   * @param availability the ax availability
   * @param companyName the company name
   * @param pickupBranchId the pickup branch id
   * @param workingHours the working hours of branch
   * @throws WorkingDayNotFoundException
   */
  private void calculateIfOutsideWorkingHoursNonSofort(Availability availability,
      final String companyName, final String pickupBranchId, final List<WorkingHours> workingHours,
      final OpeningDaysAvailabilityInfo daysAvailInfo) throws WorkingDayNotFoundException {
    Assert.notEmpty(workingHours, "Branch working hour is not available");
    final DateTime newArrivalTime;

    final DateTime axArrivalTime = new DateTime(availability.getArrivalTime());

    // #5212: [CH/AT] Calculate pick-up availability based on Calendar and opening times
    // If the availability date is a non-working day then take
    // the next working day from the Connect Opening Day calendar and the opening time
    // from the branch opening times.
    daysAvailInfo.setDate(axArrivalTime.toDate());
    if (axHolidaysChecker.isNonWorkingDay(daysAvailInfo)
        || !isBranchWorkingDay(workingHours, axArrivalTime)) {
      final Date nextWorkingDate = findInternalNextWorkingDate(axArrivalTime.toDate(),
          daysAvailInfo.getCountryName(), companyName, pickupBranchId);
      final Optional<WorkingHours> nextWorkingWeekDay =
          getWorkingWeekDay(workingHours, nextWorkingDate);
      if (!nextWorkingWeekDay.isPresent()) {
        throw new WorkingDayNotFoundException("Next working day not found");
      }
      final LocalTime nextOpeningTime =
          nextWorkingWeekDay.map(WorkingHours::getOpeningTime).orElse(null);
      newArrivalTime = resetToOpenTime(nextWorkingDate, nextOpeningTime);
      availability.setArrivalTime(
          AxArticleUtils.toUTCArrivalTime(newArrivalTime, nextOpeningTime));
      return;
    }

    final Optional<WorkingHours> workingWeekDay =
        getWorkingWeekDay(workingHours, axArrivalTime.toDate());
    if (!workingWeekDay.isPresent()) {
      throw new WorkingDayNotFoundException("Next working day from ax arrival time not found");
    }

    WorkingHours workingHour = workingWeekDay.get();
    final LocalTime openingTime = workingHour.getOpeningTime();
    final DateTime openTimeOfArrivalDate = axArrivalTime.withTime(openingTime);
    if (axArrivalTime.isBefore(openTimeOfArrivalDate)) {
      newArrivalTime = openTimeOfArrivalDate;
      availability.setArrivalTime(
          AxArticleUtils.toUTCArrivalTime(newArrivalTime, openingTime));
      return;
    }

    final DateTime closeTimeOfArrivalDate = axArrivalTime.withTime(workingHour.getClosingTime());
    if (axArrivalTime.isAfter(closeTimeOfArrivalDate)) {
      Date nextWorkingDate = nextWrkDateHelper.getNextWorkingDate(companyName, pickupBranchId,
          axArrivalTime.toDate());

      daysAvailInfo.setDate(nextWorkingDate);
      if (axHolidaysChecker.isNonWorkingDay(daysAvailInfo)
          || !isBranchWorkingDay(workingHours, new DateTime(nextWorkingDate))) {
        nextWorkingDate = findInternalNextWorkingDate(nextWorkingDate,
            daysAvailInfo.getCountryName(), companyName, pickupBranchId);
      }
      final Optional<WorkingHours> nextWorkingWeekDay =
          getWorkingWeekDay(workingHours, nextWorkingDate);
      if (!nextWorkingWeekDay.isPresent()) {
        throw new WorkingDayNotFoundException("Next working day from next working date not found");
      }

      final WorkingHours nextWorkingHour = nextWorkingWeekDay.get();
      final LocalTime nextWorkingDayOpeningTime = nextWorkingHour.getOpeningTime();

      log.debug("calculateIfOutsideWorkingHoursNonSofort next working date is {}", nextWorkingDate);
      newArrivalTime = resetToOpenTime(nextWorkingDate, nextWorkingDayOpeningTime);
      availability.setArrivalTime(
          AxArticleUtils.toUTCArrivalTime(newArrivalTime, nextWorkingDayOpeningTime));
      return;
    }

    final LocalTime time = axArrivalTime.toLocalTime();
    if (isLunchTime(time, workingHour.getLunchStartTime(), workingHour.getLunchEndTime())) {
      newArrivalTime = resetToOpenTime(axArrivalTime.toDate(), workingHour.getLunchEndTime());
      availability.setArrivalTime(
          AxArticleUtils.toUTCArrivalTime(newArrivalTime, openingTime));
    }
  }

  private Date findInternalNextWorkingDate(Date date, String countryName,
      String compName, String branchId) {
    return openingDaysRepo.findNextWorkingDay(date, countryName, compName, branchId)
        .orElseGet(Calendar.getInstance()::getTime);
  }

}
