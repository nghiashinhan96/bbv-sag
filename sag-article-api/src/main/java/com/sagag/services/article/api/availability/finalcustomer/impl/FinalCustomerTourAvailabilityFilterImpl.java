package com.sagag.services.article.api.availability.finalcustomer.impl;

import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.WssDeliveryProfileDay;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.dto.WssBranchDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileToursDto;
import com.sagag.services.domain.eshop.dto.WssTourTimesDto;
import com.sagag.services.domain.sag.erp.Availability;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class FinalCustomerTourAvailabilityFilterImpl
    extends AbstractFinalCustomerAvailabilityFilter {

  @Override
  public ErpSendMethodEnum sendMethod() {
    return ErpSendMethodEnum.TOUR;
  }

  @Override
  public Availability filterAvailability(Availability latestAvailability,
      WssDeliveryProfileDto wssDeliveryProfile, Integer wssMaxAvailabilityDayRange) {
    if (checkWssMaxAvailabilityDayRange(wssMaxAvailabilityDayRange)) {
      return null;
    }
    final WssBranchDto wssBranch = wssDeliveryProfile.getWssBranch();
    final LocalDateTime arrivalTime =
        DateUtils.toLocalDateTime(latestAvailability.getDateTimeArrivalTime());
    final List<WssDeliveryProfileToursDto> deliveryProfileTours =
        wssDeliveryProfile.getWssDeliveryProfileToursDtos().stream()
            .filter(deliveryProfileToursCanDeliverArticlePredicate()).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(deliveryProfileTours)) {
      return null;
    }

    Availability updatedAvailability =
        findWssTourCanDeliverArticle(arrivalTime, deliveryProfileTours, wssBranch.getBranchNr(),
            wssBranch.getOrgId(), wssMaxAvailabilityDayRange);
    if (updatedAvailability != null) {
      updatedAvailability.setSendMethodCode(ErpSendMethodEnum.TOUR.name());
    }
    return updatedAvailability;
  }

  private Availability findWssTourCanDeliverArticle(LocalDateTime arrivalTime,
      List<WssDeliveryProfileToursDto> deliveryProfileToursItems, Integer branchNr, Integer orgId,
      Integer wssMaxAvailabilityDayRange) {

    List<WssDeliveryProfileToursDto> deliveryProfileToursInArrivalDate =
        deliveryProfileToursItems.stream().filter(isSupplierTourTimeAfterArrivalTime(arrivalTime))
            .filter(anyWssTourOnArrivalDate(arrivalTime.toLocalDate())).sorted(deliveryProfileTourComparator())
            .collect(Collectors.toList());
    if (CollectionUtils.isNotEmpty(deliveryProfileToursInArrivalDate)) {
      final boolean isDeliveredSameDay = true;
      Availability avail = findAvailabilityInArrivalTime(arrivalTime.toLocalDate(), branchNr, orgId,
          deliveryProfileToursInArrivalDate, isDeliveredSameDay);
      if (avail != null
          && wssOpeningDaysCalendarRepo.checkExistingWorkingDayByDateAndOrgIdAndBranchNr(
              DateUtils.toLocalDateTime(avail.getDateTimeArrivalTime()).toLocalDate(), orgId,
              branchNr)) {
        return avail;
      }
    }

    return findAvailabilityInNextWorkDay(arrivalTime, deliveryProfileToursItems, branchNr, orgId,
        wssMaxAvailabilityDayRange);
  }

  private Availability findAvailabilityInNextWorkDay(LocalDateTime arrivalTime,
      List<WssDeliveryProfileToursDto> deliveryProfileToursItems, Integer branchNr, Integer orgId,
      Integer wssMaxAvailabilityDayRange) {
    LocalDate originalArrivalDate = arrivalTime.toLocalDate();
    LocalDate nextLocalDateTimeCanDeliverArticle = arrivalTime.toLocalDate().plusDays(1);
    nextLocalDateTimeCanDeliverArticle.atStartOfDay();
    while (nextLocalDateTimeCanDeliverArticle != null) {
      if (isOverWssMaxAvailabilityDayRange(originalArrivalDate,
          nextLocalDateTimeCanDeliverArticle, wssMaxAvailabilityDayRange)) {
        return null;
      }

      List<WssDeliveryProfileToursDto> deliveryProfileToursInArrivalDate = deliveryProfileToursItems
          .stream().filter(anyWssTourOnArrivalDate(nextLocalDateTimeCanDeliverArticle))
          .sorted(deliveryProfileTourComparator()).collect(Collectors.toList());
      if (CollectionUtils.isEmpty(deliveryProfileToursInArrivalDate)) {
        nextLocalDateTimeCanDeliverArticle = nextLocalDateTimeCanDeliverArticle.plusDays(1);
        continue;
      }
      final boolean isDeliveredSameDay = false;
      Availability availInNextWorkingDay =
          findAvailabilityInArrivalTime(nextLocalDateTimeCanDeliverArticle, branchNr, orgId,
              deliveryProfileToursInArrivalDate, isDeliveredSameDay);
      if (availInNextWorkingDay == null) {
        nextLocalDateTimeCanDeliverArticle = nextLocalDateTimeCanDeliverArticle.plusDays(1);
        continue;
      }
      return availInNextWorkingDay;
    }
    return null;
  }

  private Availability findAvailabilityInArrivalTime(LocalDate arrivalTime, Integer branchNr,
      Integer orgId, List<WssDeliveryProfileToursDto> deliveryProfileToursInArrivalDate,
      boolean isDeliveredSameDay) {
    for (WssDeliveryProfileToursDto wssDeliveryProfileToursDto : deliveryProfileToursInArrivalDate) {
      List<WssTourTimesDto> wssTourTimes =
          wssDeliveryProfileToursDto.getWssTour().getWssTourTimesDtos();
      if (wssDeliveryProfileToursDto.isOverNight()) {

        List<String> nextAvailabilityWeekdays = ListUtils
            .emptyIfNull(wssDeliveryProfileToursDto.getWssTour().getWssTourTimesDtos()).stream()
            .map(wssTourTime -> wssTourTime.getWeekDay().name()).collect(Collectors.toList());
        Optional<Date> overNightDeliveryDateOpt =
            wssOpeningDaysCalendarRepo.findNextWorkingDayLaterFromAndInWeekdays(
                arrivalTime, orgId, branchNr, nextAvailabilityWeekdays);

        if (overNightDeliveryDateOpt.isPresent()) {
          LocalDate overNightDeliveryDate = overNightDeliveryDateOpt.get().toLocalDate();
          String overNightTourTimeDepartureTimeStr =
              getOverNightTourDepartureTime(wssTourTimes, overNightDeliveryDate);
          if (StringUtils.isBlank(overNightTourTimeDepartureTimeStr)) {
            continue;
          }
          LocalDateTime deliveryLocalDateTime = overNightDeliveryDate
              .atTime(DateUtils.toLocalTime(overNightTourTimeDepartureTimeStr));
          return Availability.builder().tourName(wssDeliveryProfileToursDto.getTourName())
              .arrivalTime(toUTCArrivalTime(deliveryLocalDateTime)).build();
        }
      } else {
        if (!wssOpeningDaysCalendarRepo.checkExistingWorkingDayByDateAndOrgIdAndBranchNr(
            arrivalTime, orgId, branchNr)) {
          return null;
        }
        String onArrivalDateDepartureTimeStr =
            getTourTimeDepartureTimeAtDeliveryDate(wssTourTimes, arrivalTime,
                wssDeliveryProfileToursDto.getSupplierTourTime(), isDeliveredSameDay);
        if (StringUtils.isBlank(onArrivalDateDepartureTimeStr)) {
          continue;
        }
        LocalDateTime deliveryLocalDateTime =
            arrivalTime.atTime(DateUtils.toLocalTime(onArrivalDateDepartureTimeStr));
        return Availability.builder().tourName(wssDeliveryProfileToursDto.getTourName())
            .arrivalTime(toUTCArrivalTime(deliveryLocalDateTime)).build();
      }
    }
    return null;
  }

  private String getOverNightTourDepartureTime(List<WssTourTimesDto> wssTourTimes,
      LocalDate overNightDeliveryDate) {
    return wssTourTimes.stream()
        .filter(tourTime -> tourTime.getWeekDay().getValue() == overNightDeliveryDate.getDayOfWeek()
            .getValue())
        .findFirst().map(WssTourTimesDto::getDepartureTime).orElse(StringUtils.EMPTY);
  }

  private Predicate<? super WssDeliveryProfileToursDto> isSupplierTourTimeAfterArrivalTime(
      final LocalDateTime arrivalTime) {
    return tourAssignment -> !DateUtils.toLocalTime(tourAssignment.getSupplierTourTime())
        .isBefore(arrivalTime.toLocalTime());
  }

  private Predicate<? super WssDeliveryProfileToursDto> anyWssTourOnArrivalDate(
      final LocalDate arrivalTime) {
    return tourAssignment -> tourAssignment.getSupplierTourDay() == WssDeliveryProfileDay.ALL
        || tourAssignment.getSupplierTourDay().getValue() == arrivalTime.getDayOfWeek().getValue();
  }

  private String getTourTimeDepartureTimeAtDeliveryDate(List<WssTourTimesDto> wssTourTimes,
      LocalDate deliveryLocalDate, String supplierTourTime, boolean isDeliveredSameday) {
    Optional<WssTourTimesDto> tourTimesOpt = wssTourTimes.stream()
        .filter(
            filterTourTimesAtArrivalDate(deliveryLocalDate, supplierTourTime, isDeliveredSameday))
        .findFirst();
    if (tourTimesOpt.isPresent()) {
      return tourTimesOpt.get().getDepartureTime();
    }
    return null;
  }

  private Predicate<? super WssTourTimesDto> filterTourTimesAtArrivalDate(
      LocalDate deliveryLocalDate, String supplierTourTime, boolean isDeliveredSameday) {
    return tourTime -> tourTime.getWeekDay().getValue() == deliveryLocalDate.getDayOfWeek()
        .getValue()
        && !(isDeliveredSameday && DateUtils.toLocalTime(tourTime.getDepartureTime())
            .isBefore(DateUtils.toLocalTime(supplierTourTime)));
  }

  private Predicate<WssDeliveryProfileToursDto> deliveryProfileToursCanDeliverArticlePredicate() {
    return deliveryProfileTours -> deliveryProfileTours != null
        && deliveryProfileTours.getWssTour() != null
        && CollectionUtils.isNotEmpty(deliveryProfileTours.getWssTour().getWssTourTimesDtos())
        && isDeliveryProfileToursHasValidTourTime(deliveryProfileTours);

  }

  private boolean isDeliveryProfileToursHasValidTourTime(
      WssDeliveryProfileToursDto deliveryProfileTours) {
    return deliveryProfileTours.getSupplierTourDay() == WssDeliveryProfileDay.ALL
        || deliveryProfileTours.getWssTour().getWssTourTimesDtos().stream()
            .anyMatch(isValidTourTimeInSameDay(deliveryProfileTours))
        || (deliveryProfileTours.isOverNight()
            && deliveryProfileTours.getWssTour().getWssTourTimesDtos().stream()
                .anyMatch(isTourTimeDayDifferentWithDeliveryProfileTour(deliveryProfileTours)));
  }

  private Predicate<? super WssTourTimesDto> isTourTimeDayDifferentWithDeliveryProfileTour(
      WssDeliveryProfileToursDto deliveryProfileTours) {
    return tourTime -> tourTime.getWeekDay().getValue() != deliveryProfileTours.getSupplierTourDay()
        .getValue();
  }

  private Predicate<? super WssTourTimesDto> isValidTourTimeInSameDay(
      WssDeliveryProfileToursDto deliveryProfileTours) {
    return tourTimes -> tourTimes.getWeekDay().getValue() == deliveryProfileTours
        .getSupplierTourDay().getValue()
        && DateUtils.toLocalTime(tourTimes.getDepartureTime())
            .isAfter(DateUtils.toLocalTime(deliveryProfileTours.getSupplierTourTime()));
  }

}
