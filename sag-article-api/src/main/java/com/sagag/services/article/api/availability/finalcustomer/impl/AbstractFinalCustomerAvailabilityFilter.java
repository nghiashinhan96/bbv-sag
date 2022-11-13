package com.sagag.services.article.api.availability.finalcustomer.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sagag.eshop.repo.api.WssOpeningDaysCalendarRepository;
import com.sagag.services.common.enums.WssDeliveryProfileDay;
import com.sagag.services.article.api.availability.finalcustomer.FinalCustomerAvailabilityFilter;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.dto.WssBranchDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileToursDto;

public abstract class AbstractFinalCustomerAvailabilityFilter
    implements FinalCustomerAvailabilityFilter {

  @Autowired
  protected WssOpeningDaysCalendarRepository wssOpeningDaysCalendarRepo;


  /**
   * Formats arrival time in UTC.
   *
   * @param arrivalTime the date time
   */
  protected String toUTCArrivalTime(LocalDateTime arrivalTime) {
    if (arrivalTime == null) {
      return StringUtils.EMPTY;
    }
    ZonedDateTime arrivalTimeInUTC =
        arrivalTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
    return arrivalTimeInUTC.format(DateTimeFormatter.ofPattern(DateUtils.UTC_DATE_PATTERN));
  }

  protected Comparator<? super WssDeliveryProfileToursDto> deliveryProfileTourComparator() {
    return Comparator
        .comparing(
            (WssDeliveryProfileToursDto tour) -> DateUtils.toLocalTime(tour.getSupplierTourTime()))
        .thenComparing((WssDeliveryProfileToursDto tour) -> tour.getSupplierTourDay(),
            Comparator.reverseOrder());
  }

  protected List<String> extractDeliverProfileTourWeekdays(
      List<WssDeliveryProfileToursDto> deliveryProfileToursItems) {
    if (deliveryProfileToursItems.stream().anyMatch(
        tourAssignment -> WssDeliveryProfileDay.ALL == tourAssignment.getSupplierTourDay())) {
      return Arrays.stream(WeekDay.values()).map(Enum::name).collect(Collectors.toList());
    }
    return deliveryProfileToursItems.stream()
        .map(tourAssignment -> tourAssignment.getSupplierTourDay().name())
        .collect(Collectors.toList());
  }

  protected List<String> extractDeliverProfilePickupWeekdays(
      List<WssDeliveryProfileToursDto> deliveryProfileToursItems, WssBranchDto wssBranchDto) {
    final List<String> branchOpeningWeekDays = wssBranchDto.getWssBranchOpeningWeekDays();
    if (deliveryProfileToursItems.stream().anyMatch(
        tourAssignment -> WssDeliveryProfileDay.ALL == tourAssignment.getSupplierTourDay())) {
      return branchOpeningWeekDays;
    }
    return deliveryProfileToursItems.stream()
        .filter(wssDeliveryProfileToursDto -> branchOpeningWeekDays
            .contains(wssDeliveryProfileToursDto.getSupplierTourDay().name()))
        .map(tourAssignment -> tourAssignment.getSupplierTourDay().name())
        .collect(Collectors.toList());
  }

  protected boolean isOverWssMaxAvailabilityDayRange(LocalDate originalArrivalDate,
      LocalDate targetAvailabilityDate, Integer wssMaxAvailabilityDayRange) {
    if (originalArrivalDate == null || targetAvailabilityDate == null
        || wssMaxAvailabilityDayRange == null) {
      return false;
    }
    long daysBetween = ChronoUnit.DAYS.between(originalArrivalDate, targetAvailabilityDate);
    return daysBetween > wssMaxAvailabilityDayRange;
  }

  protected boolean checkWssMaxAvailabilityDayRange(Integer wssMaxAvailabilityDayRange) {
    return wssMaxAvailabilityDayRange == null || wssMaxAvailabilityDayRange == 0;
  }
}
