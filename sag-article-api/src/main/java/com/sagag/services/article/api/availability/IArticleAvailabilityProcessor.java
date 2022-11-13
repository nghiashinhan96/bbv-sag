package com.sagag.services.article.api.availability;

import static com.sagag.services.common.enums.ErpSendMethodEnum.PICKUP;
import static com.sagag.services.common.enums.ErpSendMethodEnum.TOUR;

import com.sagag.services.article.api.utils.ArticleApiUtils;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.domain.sag.erp.Availability;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.function.Predicate;

/**
 * Interface to provide implementation for availability.
 *
 */
public interface IArticleAvailabilityProcessor {

  /**
   * Returns the article availability result per countries.
   *
   * @param availability
   * @param sendMethod
   * @param nextWorkingTour
   * @return the result of {@link ArticleAvailabilityResult}
   */
  ArticleAvailabilityResult process(Availability availability, ErpSendMethodEnum sendMethod,
      DateTime nextWorkingTour);

  /**
   * Returns the default availability result per countries.
   *
   * @return the default result of {@link ArticleAvailabilityResult}
   */
  ArticleAvailabilityResult getDefaultResult();

  ArticleAvailabilityResult getDefaultResultForNoPrice();

  default boolean isDeliveryImmediate(final ErpSendMethodEnum sendMethod,
      final DateTime arrivalTime, final Boolean immediateDelivery) {
    return (sendMethod == PICKUP && immediateDelivery) || arrivalTime.isBeforeNow();
  }

  /**
   * if (deliveryType = TOUR and av.arrivalTime.date = TODAY): then myState = SAME_DAY.
   *
   * if deliveryType = COLLECT and av.immediateDelivery = true or av.arrivalTime < time@now then
   * myState = SAME_DAY
   *
   * (Note: .date means the year.month.day and not hours or minutes)
   */
  default boolean isDeliverySameDay(final ErpSendMethodEnum sendMethod,
      final DateTime arrivalTime) {
    LocalDate arrivalDate = arrivalTime.toLocalDate();
    return (sendMethod == TOUR || sendMethod == PICKUP) && arrivalDate.isEqual(LocalDate.now());
  }

  default boolean isDeliveryNextWorkingDay(final ErpSendMethodEnum sendMethod,
      final DateTime arrivalTime, final DateTime nextWorkingStartTime) {
    final LocalDate lastTourDate = nextWorkingStartTime.toLocalDate();
    final LocalDate arrivalDate = arrivalTime.toLocalDate();
    return (sendMethod == TOUR || sendMethod == PICKUP) && arrivalDate.equals(lastTourDate);
  }

  default Predicate<Availability> validArrivalTimePredicatce() {
    return ArticleApiUtils::isValidArrivalTime;
  }

}
