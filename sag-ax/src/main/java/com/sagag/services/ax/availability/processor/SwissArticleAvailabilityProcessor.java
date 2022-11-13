package com.sagag.services.ax.availability.processor;

import static com.sagag.services.common.enums.ErpSendMethodEnum.PICKUP;
import static com.sagag.services.common.enums.ErpSendMethodEnum.TOUR;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.ax.enums.SwissArticleAvailabilityState;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.profiles.ChProfile;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.extern.slf4j.Slf4j;

/**
 * Processes article availabilities for CH.
 *
 */
@Component
@ChProfile
@Slf4j
public class SwissArticleAvailabilityProcessor extends AxArticleAvailabilityProcessor {

  @Override
  public ArticleAvailabilityResult process(final Availability availability,
      final ErpSendMethodEnum sendMethod, final DateTime nextWorkingTour) {

    // in case has no availabilityArticleAvailability
    if (!hasAvailability(availability)) {
      return SwissArticleAvailabilityState.EXTENDED.toResult();
    }

    if (!hasNextWorkingTour(nextWorkingTour)) {
      return SwissArticleAvailabilityState.EXTENDED.toResult();
    }

    log.debug("Back Order {}", availability.getBackOrder());
    // |yellow|yellow|Mo-So / kein Bestand = Zwischenkauf - "Backorder ==TRUE" && Stock = 0|
    if (isBackOrder(availability.getBackOrder())) {
      // #4060: [CH-AX] Final optimizations of CH-AX : Update availability to be up-to-date
      return SwissArticleAvailabilityState.IN_24_HOURS.toResult();
    }

    final String arrivalTimeStr = availability.getArrivalTime();
    log.debug("ArrivalTime {}", arrivalTimeStr);
    if (!hasArrivalTime(arrivalTimeStr)) {
      return SwissArticleAvailabilityState.EXTENDED.toResult();
    }

    Boolean immediateDelivery = availability.isImmediateDelivery();
    log.debug("immediateDelivery {}", immediateDelivery);
    DateTime arrivalTime = new DateTime(arrivalTimeStr);
    if (isDeliveryImmediate(sendMethod, arrivalTime, immediateDelivery)) {
      return SwissArticleAvailabilityState.IMMEDIATE.toResult();
    }

    if (isDeliveryTourPlusOne(sendMethod, arrivalTime, nextWorkingTour)) {
      return SwissArticleAvailabilityState.TOUR_PLUS_ONE.toResult();
    }

    if (isDeliverySameDay(sendMethod, arrivalTime)) {
      return SwissArticleAvailabilityState.SAME_DAY.toResult();
    }

    if (isDeliveryNextWorkingDay(sendMethod, arrivalTime, nextWorkingTour)) {
      return SwissArticleAvailabilityState.NEXT_DAY.toResult();
    }

    return SwissArticleAvailabilityState.EXTENDED.toResult();
  }

  public static Boolean isDeliveryTourPlusOne(ErpSendMethodEnum sendMethod, DateTime arrivalTime,
      DateTime newStartTime) {
    return !(sendMethod != TOUR && sendMethod != PICKUP) && arrivalTime.isBefore(newStartTime);
  }

  @Override
  public ArticleAvailabilityResult getDefaultResult() {
    return SwissArticleAvailabilityState.EXTENDED.toResult();
  }

  @Override
  public ArticleAvailabilityResult getDefaultResultForNoPrice() {
    return SwissArticleAvailabilityState.IN_24_HOURS.toResult();
  }
}
