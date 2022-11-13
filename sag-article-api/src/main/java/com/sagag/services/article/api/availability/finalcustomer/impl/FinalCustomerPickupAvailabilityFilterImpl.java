package com.sagag.services.article.api.availability.finalcustomer.impl;

import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.common.enums.WssDeliveryProfileDay;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.dto.WssBranchDto;
import com.sagag.services.domain.eshop.dto.WssBranchOpeningTimeDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileToursDto;
import com.sagag.services.domain.sag.erp.Availability;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class FinalCustomerPickupAvailabilityFilterImpl
    extends AbstractFinalCustomerAvailabilityFilter {

  @Override
  public ErpSendMethodEnum sendMethod() {
    return ErpSendMethodEnum.PICKUP;
  }

  @Override
  public Availability filterAvailability(Availability latestAvailability,
      WssDeliveryProfileDto wssDeliveryProfile, Integer wssMaxAvailabilityDayRange) {
    if (checkWssMaxAvailabilityDayRange(wssMaxAvailabilityDayRange)) {
      return null;
    }
    final LocalDateTime arrivalTime =
        DateUtils.toLocalDateTime(latestAvailability.getDateTimeArrivalTime());
    if (CollectionUtils.isEmpty(wssDeliveryProfile.getWssDeliveryProfileToursDtos())) {
      return null;
    }
    final WssBranchDto wssBranch = wssDeliveryProfile.getWssBranch();
    if (wssBranch == null || CollectionUtils.isEmpty(wssBranch.getWssBranchOpeningTimes())) {
      return null;
    }
    final Optional<LocalDateTime> pickUpTimeFromDeliveryProfileTourOpt =
        findDateTimeCanPickUpArticle(arrivalTime,
            wssDeliveryProfile.getWssDeliveryProfileToursDtos());

    if (!pickUpTimeFromDeliveryProfileTourOpt.isPresent()) {
      return null;
    }

    final LocalDateTime pickupDateTime = pickUpTimeFromDeliveryProfileTourOpt.get();

    Optional<LocalDateTime> checkedPickupDateTime =
        checkDateTimeCanPickUpArticle(pickupDateTime, wssBranch);

    if (!checkedPickupDateTime.isPresent()
        || isOverWssMaxAvailabilityDayRange(arrivalTime.toLocalDate(),
            checkedPickupDateTime.get().toLocalDate(), wssMaxAvailabilityDayRange)) {
      return null;
    }
    final Availability availability = new Availability();
    availability.setSendMethodCode(ErpSendMethodEnum.PICKUP.name());
    availability.setArrivalTime(toUTCArrivalTime(checkedPickupDateTime.get()));
    return availability;
  }

  private Optional<LocalDateTime> findDateTimeCanPickUpArticle(LocalDateTime arrivalTime,
      List<WssDeliveryProfileToursDto> deliveryProfileToursItems) {

    Optional<WssDeliveryProfileToursDto> deliveryProfileTourForPickupOpt = deliveryProfileToursItems
        .stream().filter(anyPickupDeliveryProfileTourInArrivalDate(arrivalTime))
        .min(deliveryProfileTourComparator());
    return deliveryProfileTourForPickupOpt
        .map(wssDeliveryProfileToursDto -> getPickupTimeFromDeliveryProfileTour(arrivalTime,
            wssDeliveryProfileToursDto))
        .orElseGet(() -> findPickupTimeInNextWorkday(arrivalTime, deliveryProfileToursItems));
  }

  private boolean isWssBranchWorkingDay(LocalDateTime arrivalTime, WssBranchDto wssBranchDto) {
    return CollectionUtils.isNotEmpty(wssBranchDto.getWssBranchOpeningTimes())
        && wssBranchDto.getWssBranchOpeningTimes().stream()
            .anyMatch(wssBranchOpeningTimeDto -> wssBranchOpeningTimeDto.getWeekDay() == WeekDay
                .valueOf(arrivalTime.getDayOfWeek().name()));
  }

  private Optional<LocalDateTime> findPickupTimeInNextWorkday(LocalDateTime arrivalTime,
      List<WssDeliveryProfileToursDto> deliveryProfileToursItems) {

    WeekDay arrivalWeekDay = WeekDay.valueOf(arrivalTime.getDayOfWeek().name());
    List<WeekDay> weekDays = WeekDay.buildNextWeekDayList(arrivalWeekDay);
    for (WeekDay weekDay : weekDays) {
      Optional<WssDeliveryProfileToursDto> matchedDeliveryProfileTourOpt = deliveryProfileToursItems
          .parallelStream().filter(anyDeliveryProfileTourForPickupAfterArrivalDate(weekDay))
          .min(deliveryProfileTourComparator());
      if (matchedDeliveryProfileTourOpt.isPresent()) {
        return getPickupTimeFromDeliveryProfileTour(arrivalTime,
            matchedDeliveryProfileTourOpt.get());
      }
    }
    return Optional.empty();
  }

  private Predicate<WssDeliveryProfileToursDto> anyDeliveryProfileTourForPickupAfterArrivalDate(
      WeekDay weekDay) {
    return wssDeliveryProfileToursDto -> wssDeliveryProfileToursDto.getPickupWaitDuration() != null
        && (wssDeliveryProfileToursDto.getSupplierTourDay() == WssDeliveryProfileDay.ALL
            || wssDeliveryProfileToursDto.getSupplierTourDay().getValue() == weekDay.getValue());
  }

  private Optional<LocalDateTime> getPickupTimeFromDeliveryProfileTour(LocalDateTime arrivalTime,
      WssDeliveryProfileToursDto deliveryProfileTourForPickup) {
    return Optional.of(
        arrivalTime.plusSeconds(deliveryProfileTourForPickup.getPickupWaitDuration().longValue()));
  }

  private Predicate<? super WssDeliveryProfileToursDto> anyPickupDeliveryProfileTourInArrivalDate(
      final LocalDateTime arrivalTime) {
    return tourAssignment -> tourAssignment.getPickupWaitDuration() != null
        && !DateUtils.toLocalTime(tourAssignment.getSupplierTourTime())
            .isBefore(arrivalTime.toLocalTime())
        && (WssDeliveryProfileDay.ALL == tourAssignment.getSupplierTourDay() || tourAssignment
            .getSupplierTourDay().getValue() == arrivalTime.getDayOfWeek().getValue());
  }

  private Optional<LocalDateTime> checkDateTimeCanPickUpArticle(LocalDateTime pickupDateTime,
      WssBranchDto wssBranch) {
    Assert.notNull(pickupDateTime, "pickupDateTime is required");
    Assert.notNull(wssBranch, "wssBranch is required");

    final boolean isWssWorkingDay =
        wssOpeningDaysCalendarRepo.checkExistingWorkingDayByDateAndOrgIdAndBranchNr(
            pickupDateTime.toLocalDate(), wssBranch.getOrgId(), wssBranch.getBranchNr());
    final boolean isWssBranchWorkingDay = isWssBranchWorkingDay(pickupDateTime, wssBranch);
    if (!isWssWorkingDay || !isWssBranchWorkingDay) {
      return findWssBranchNextOpeningTime(pickupDateTime, wssBranch);
    }
    final List<WssBranchOpeningTimeDto> wssBranchOpeningTimes =
        wssBranch.getWssBranchOpeningTimes();
    if (!isWssBranchHasOpeningTimes(wssBranchOpeningTimes)) {
      return Optional.empty();
    }
    WeekDay arrivalWeekDay = WeekDay.valueOf(pickupDateTime.getDayOfWeek().name());
    Optional<WssBranchOpeningTimeDto> wssBranchOpeningTimeDtoOnArrivalOpt =
        wssBranchOpeningTimes.stream()
            .filter(
                wssBranchOpeningTimeDto -> arrivalWeekDay == wssBranchOpeningTimeDto.getWeekDay())
            .findFirst();
    if (!wssBranchOpeningTimeDtoOnArrivalOpt.isPresent()) {
      return Optional.empty();
    }
    WssBranchOpeningTimeDto wssBranchOpeningTimeDtoOnArrival =
        wssBranchOpeningTimeDtoOnArrivalOpt.get();
    final LocalTime openHour =
        DateUtils.toLocalTime(wssBranchOpeningTimeDtoOnArrival.getOpeningTime());
    final LocalTime closeHour =
        DateUtils.toLocalTime(wssBranchOpeningTimeDtoOnArrival.getClosingTime());
    final LocalTime lunchStartHour =
        DateUtils.toLocalTime(wssBranchOpeningTimeDtoOnArrival.getLunchStartTime());
    final LocalTime lunchEndHour =
        DateUtils.toLocalTime(wssBranchOpeningTimeDtoOnArrival.getLunchEndTime());
    LocalTime pickupTime = pickupDateTime.toLocalTime();
    if (pickupTime.isBefore(openHour)) {
      return Optional.of(pickupDateTime.toLocalDate().atTime(openHour));
    }
    if (pickupTime.isAfter(closeHour)) {
      return findWssBranchNextOpeningTime(pickupDateTime, wssBranch);
    }
    if (DateUtils.isTimeBetweenDuration(pickupTime, lunchStartHour, lunchEndHour)) {
      return Optional.of(pickupDateTime.toLocalDate().atTime(lunchEndHour));
    }
    return Optional.of(pickupDateTime);
  }

  private Optional<LocalDateTime> findWssBranchNextOpeningTime(LocalDateTime pickupDateTime,
      WssBranchDto wssBranch) {
    Date nextWorkingDate = wssOpeningDaysCalendarRepo
        .findNextWorkingDayLaterFromAndInWeekdays(pickupDateTime.toLocalDate(),
            wssBranch.getOrgId(), wssBranch.getBranchNr(), wssBranch.getWssBranchOpeningWeekDays())
        .orElse(null);
    if (nextWorkingDate == null) {
      return Optional.empty();
    }
    WeekDay nextBranchOpeningWeekDay =
        WeekDay.valueOf(nextWorkingDate.toLocalDate().getDayOfWeek().name());
    WssBranchOpeningTimeDto wssBranchOpeningTimeDto =
        wssBranch.getWssBranchOpeningTimeOnWeekDay(nextBranchOpeningWeekDay).orElse(null);
    if (wssBranchOpeningTimeDto == null
        || StringUtils.isBlank(wssBranchOpeningTimeDto.getOpeningTime())) {
      return Optional.empty();
    }
    return Optional.of(nextWorkingDate.toLocalDate()
        .atTime(DateUtils.toLocalTime(wssBranchOpeningTimeDto.getOpeningTime())));
  }

  private boolean isWssBranchHasOpeningTimes(List<WssBranchOpeningTimeDto> wssBranchOpeningTimes) {
    return CollectionUtils.isNotEmpty(wssBranchOpeningTimes)
        && wssBranchOpeningTimes.stream().anyMatch(wssBranchOpeningTimeDto -> StringUtils
            .isNotBlank(wssBranchOpeningTimeDto.getOpeningTime()));
  }
}
