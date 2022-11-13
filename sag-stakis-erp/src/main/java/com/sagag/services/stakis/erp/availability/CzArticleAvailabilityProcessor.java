package com.sagag.services.stakis.erp.availability;

import java.util.Objects;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.IArticleAvailabilityProcessor;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.stakis.erp.enums.StakisArticleAvailabilityState;

import lombok.extern.slf4j.Slf4j;

/**
 * Processes article availabilities for CZ.
 *
 */
@Component
@CzProfile
@Slf4j
public class CzArticleAvailabilityProcessor implements IArticleAvailabilityProcessor {
  @Override
  public ArticleAvailabilityResult process(final Availability availability,
      final ErpSendMethodEnum sendMethod, final DateTime nextWorkingTour) {
    if (!hasAvailability(availability)) {
      return StakisArticleAvailabilityState.YELLOW.toResult();
    }

    if (!hasNextWorkingTour(nextWorkingTour)) {
      return StakisArticleAvailabilityState.YELLOW.toResult();
    }

    final String arrivalTimeStr = availability.getArrivalTime();
    log.debug("ArrivalTime {}", arrivalTimeStr);
    if (!hasArrivalTime(arrivalTimeStr)) {
      return StakisArticleAvailabilityState.YELLOW.toResult();
    }
    Boolean immediateDelivery = availability.isImmediateDelivery();
    log.debug("immediateDelivery {}", immediateDelivery);
    DateTime arrivalTime = new DateTime(arrivalTimeStr);

    if (isDeliveryImmediate(sendMethod, arrivalTime, immediateDelivery)) {
      return StakisArticleAvailabilityState.GREEN.toResult();
    }

    if (isDeliverySameDay(sendMethod, arrivalTime)) {
      return StakisArticleAvailabilityState.GREEN.toResult();
    }

    if (isDeliveryNextWorkingDay(sendMethod, arrivalTime, nextWorkingTour)) {
      return StakisArticleAvailabilityState.YELLOW_GREEN.toResult();
    }

    return StakisArticleAvailabilityState.YELLOW.toResult();
  }

  @Override
  public ArticleAvailabilityResult getDefaultResult() {
    return StakisArticleAvailabilityState.YELLOW.toResult();
  }

  @Override
  public ArticleAvailabilityResult getDefaultResultForNoPrice() {
    return StakisArticleAvailabilityState.BLACK.toResult();
  }

  protected boolean hasAvailability(final Availability availability) {
    return Objects.nonNull(availability);
  }

  protected boolean hasNextWorkingTour(final DateTime nextWorkingTour) {
    return Objects.nonNull(nextWorkingTour);
  }

  protected boolean isBackOrder(final Boolean backOrder) {
    return Boolean.TRUE.equals(backOrder);
  }

  protected boolean hasArrivalTime(final String arrivalTime) {
    return Objects.nonNull(arrivalTime);
  }
}
