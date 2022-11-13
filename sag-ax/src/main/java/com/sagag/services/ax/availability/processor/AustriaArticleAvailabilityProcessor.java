package com.sagag.services.ax.availability.processor;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.ax.enums.AustriaArticleAvailabilityState;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.profiles.AtSbProfile;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.extern.slf4j.Slf4j;

/**
 * Processes article availabilities for AT.
 *
 */
@Component
@AtSbProfile
@Slf4j
public class AustriaArticleAvailabilityProcessor extends AxArticleAvailabilityProcessor {

  @Override
  public ArticleAvailabilityResult process(final Availability availability,
      final ErpSendMethodEnum sendMethod, final DateTime nextWorkingTour) {
    // in case has no availabilityArticleAvailability
    if (!hasAvailability(availability)) {
      return AustriaArticleAvailabilityState.YELLOW.toResult();
    }

    if (!hasNextWorkingTour(nextWorkingTour)) {
      return AustriaArticleAvailabilityState.YELLOW.toResult();
    }

    log.debug("Back Order {}", availability.getBackOrder());
    // |yellow|yellow|Mo-So / kein Bestand = Zwischenkauf - "Backorder ==TRUE" && Stock = 0|
    if (isBackOrder(availability.getBackOrder())) {
      return AustriaArticleAvailabilityState.IN_144_HOURS.toResult();
    }

    final String arrivalTimeStr = availability.getArrivalTime();
    log.debug("ArrivalTime {}", arrivalTimeStr);
    if (!hasArrivalTime(arrivalTimeStr)) {
      return AustriaArticleAvailabilityState.YELLOW.toResult();
    }
    Boolean immediateDelivery = availability.isImmediateDelivery();
    log.debug("immediateDelivery {}", immediateDelivery);
    DateTime arrivalTime = new DateTime(arrivalTimeStr);

    if (isDeliveryImmediate(sendMethod, arrivalTime, immediateDelivery)) {
      return AustriaArticleAvailabilityState.GREEN.toResult();
    }

    if (isDeliverySameDay(sendMethod, arrivalTime)) {
      return AustriaArticleAvailabilityState.GREEN.toResult();
    }

    if (isDeliveryNextWorkingDay(sendMethod, arrivalTime, nextWorkingTour)) {
      return AustriaArticleAvailabilityState.YELLOW_GREEN.toResult();
    }

    return AustriaArticleAvailabilityState.YELLOW.toResult();
  }

  @Override
  public ArticleAvailabilityResult getDefaultResult() {
    return AustriaArticleAvailabilityState.YELLOW.toResult();
  }

  @Override
  public ArticleAvailabilityResult getDefaultResultForNoPrice() {
    return AustriaArticleAvailabilityState.IN_144_HOURS.toResult();
  }

}
