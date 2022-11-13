package com.sagag.services.ax.availability.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;
import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.AvailabilityFilter;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.article.api.utils.AvailabilityFilterUtils;
import com.sagag.services.ax.availability.backorder.CompositeBackOrderAvailabilityHandler;
import com.sagag.services.ax.availability.calculator.ArrivalTimeCalculator;
import com.sagag.services.ax.availability.processor.AxArticleAvailabilityProcessor;
import com.sagag.services.ax.availability.tourtime.NextWorkingDateHelper;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.ax.utils.AxConstants;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.extern.slf4j.Slf4j;

/**
 * The component to filter availabilities for AX.
 *
 */
@Component
@Slf4j
@DynamicAxProfile
@Primary
public class AxAvailabilityFilter implements AvailabilityFilter {

  private static final String AVAIL_LOG_MSG = "Next working date = {}, is_cart_mode = {}, "
      + "Send method = {}, Pickup branch id = {}\nCurrent avail = {}";

  private static final int NEXT_48_HOURS = 48;

  @Autowired
  private AxArticleAvailabilityProcessor availabilityProcessor;

  @Autowired
  private CompositeBackOrderAvailabilityHandler backOrderAvailabilityHandler;

  @Autowired
  private List<ArrivalTimeCalculator> arrivalTimeCalculators;

  @Autowired
  private NextWorkingDateHelper nextWorkingDateHelper;

  /**
   * Filters availabilities to handle PICK/TOUR and BACK ORDER Handle out of working hours for
   * PICKUP For back order case, there will one more request to reload the current avail object with
   * tour time table from ERP AX. It is slow but so far solution. This is to use with DVSE catalog
   * request for avail and prices as well.
   *
   * @param article the request article
   * @param artCriteria the article search input
   * @param tourTimeList the tour time list
   * @param openingHours the opening hours
   * @param countryName the country name
   * @return the list of {@link Availability}
   */
  @Override
  public List<Availability> filterAvailabilities(ArticleDocDto article,
      final ArticleSearchCriteria artCriteria, List<TourTimeDto> tourTimeList,
      List<WorkingHours> openingHours, String countryName) {
    log.debug("Filtering availabilities by availabilityProcessor = {}", availabilityProcessor);

    final String deliveryType = artCriteria.getDeliveryType();
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.valueOf(deliveryType);
    final String pickupBranchId =
        StringUtils.defaultIfBlank(artCriteria.getPickupBranchId(), AxConstants.DEFAULT_BRANCH_ID);

    final Optional<Date> nextWorkingDateForToday = nextWorkingDateHelper
        .getNextWorkingDate(artCriteria.getNextWorkingDateForToday(), openingHours);

    final SupportedAffiliate affiliate = artCriteria.getAffiliate();
    final ArticleAvailabilityResult defaultResult =
        availabilityProcessor.getDefaultResultForNoPrice();
    if (noPricePredicate().test(artCriteria, article)
        && BooleanUtils.isFalse(artCriteria.isDvseMode())) {
      return AxArticleUtils.defaultAvailabilities(article, deliveryType, defaultResult);
    }

    final List<Availability> returnFilteredAvailabilities = new ArrayList<>();
    final List<Availability> availabilities = ListUtils.emptyIfNull(article.getAvailabilities());

    final boolean isCartMode = artCriteria.isCartMode();

    final NextWorkingDates nextWorkingDates = artCriteria.getNextWorkingDate();
    final DateTime next48HoursDateTime = DateTime.now().plusHours(NEXT_48_HOURS);

    DateTime lastTourTime;
    for (Availability availability : availabilities) {
      if (!availabilityProcessor.validArrivalTimePredicatce().test(availability)) {
        availability.setBackOrder(true);
        availability.setArrivalTime(
            DateUtils.getUTCDateString(next48HoursDateTime.toDate(), DateUtils.UTC_DATE_PATTERN));
      }

      // Process for case back order is true
      final boolean backOrder = backOrderTruePredicate().test(availability);
      Date nextWorkingDate =
          backOrder ? nextWorkingDates.getBackorderDate() : nextWorkingDates.getNoBackorderDate();
      log.debug(AVAIL_LOG_MSG,
          nextWorkingDate, isCartMode, sendMethod, pickupBranchId, availability);

      // Returns the last tour time
      lastTourTime = NextWorkingDateHelper
          .getLastTourTime(availability.getLastTour(), nextWorkingDate);

      log.debug("LastTourTime = {}", lastTourTime);
      if (backOrder) { // BACK ORDER !
        log.debug("Processing back order is true ...");
        AvailabilityCriteria availabilityCriteria = AvailabilityCriteria.builder()
            .lastTourTime(lastTourTime).sendMethodEnum(sendMethod).tourTimeList(tourTimeList)
            .pickupBranchId(pickupBranchId).affiliate(affiliate).nextWorkingDate(nextWorkingDate)
            .openingHours(openingHours).countryName(countryName).build();
        boolean validToContinue =
            backOrderAvailabilityHandler.handle(availability, availabilityCriteria);
        if (!validToContinue) {
          returnFilteredAvailabilities.add(availability);
          continue;
        }
      } else {

        if (sendMethod == ErpSendMethodEnum.PICKUP) {
          boolean flagOfArrivalTimeCalculation = arrivalTimeCalculators.stream()
              .filter(calculator -> calculator.sendMethodMode() == sendMethod).findFirst()
              .map(calculator -> calculator.calculateArrivalTime(availability,
                  nextWorkingDateForToday, affiliate, pickupBranchId, tourTimeList, openingHours,
                  countryName))
              .orElse(false);

          if (!flagOfArrivalTimeCalculation) {
            return AxArticleUtils.defaultAvailabilities(article, deliveryType, defaultResult);
          }
        }
      }

      backOrderAvailabilityHandler.updateAvailStateInfoIfCartMode(availability, lastTourTime,
          sendMethod, isCartMode);

      // add filter element to return list
      returnFilteredAvailabilities.add(availability);
    }

    boolean dvseModeAndNotCartMode = BooleanUtils.isTrue(artCriteria.isDvseMode()) && !isCartMode;
    filterAvailabilitiesIfDvseMode(availabilities, dvseModeAndNotCartMode);

    Collections.sort(returnFilteredAvailabilities,
        AvailabilityFilterUtils.sortByArrivalTimeIfExist());

    if (noAvailabilitiesPredicate().test(artCriteria, article)) {
      return AxArticleUtils.defaultAvailabilities(article, deliveryType, defaultResult);
    }

    return returnFilteredAvailabilities;
  }

  /**
   * DVSE won't display the split position avail handle avail displaying for DVSE catalog User
   * Interface.
   *
   * @param availabilities List<Availability>
   * @param isDvseMode boolean
   */
  private List<Availability> filterAvailabilitiesIfDvseMode(List<Availability> availabilities,
      boolean isDvseMode) {
    if (CollectionUtils.isEmpty(availabilities)) {
      return Collections.emptyList();
    }
    if (!isDvseMode) {
      return availabilities;
    }

    final Optional<Availability> availOpt =
        availabilities.stream().filter(backOrderTruePredicate()).findFirst();
    if (availOpt.isPresent()) {
      return Arrays.asList(availOpt.get());
    }

    // the last item is the latest date
    log.debug("the last item is the latest date and backorder is false");
    return Arrays.asList(Iterables.getLast(availabilities));
  }

}
