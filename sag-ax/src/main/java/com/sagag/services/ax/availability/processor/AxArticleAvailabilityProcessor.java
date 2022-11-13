package com.sagag.services.ax.availability.processor;

import java.util.Objects;

import org.joda.time.DateTime;

import com.sagag.services.article.api.availability.IArticleAvailabilityProcessor;
import com.sagag.services.domain.sag.erp.Availability;

public abstract class AxArticleAvailabilityProcessor implements IArticleAvailabilityProcessor {

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
