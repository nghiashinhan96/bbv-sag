package com.sagag.services.dvse.enums;

import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.sagag.services.ax.enums.SwissArticleAvailabilityState;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum DvseChAvailabilityState {

  GREEN(1), YELLOW(3), YELLOW_GREEN(4), YELLOW_ORANGE(7), BLUE(9);

  private int code;

  public static final String AVAIL_DESCRIPTION_MATIK =
      "*Lieferzeit anfragen, eventuell Frachtkosten*";
  public static final String AVAIL_DESCRIPTION_QWP =
      "Sonderbestellung ! Lieferzeit bis zu 48 Stunden (vorbehaltlich Verfügbarkeit)";

  private static final int MIDDAY = 12;

  public static Optional<DvseChAvailabilityState> fromAricleAvailability(Availability availability,
      DateTime nextWorkingDay, int connectAvailState) {
    if (availability == null || StringUtils.isBlank(availability.getArrivalTime())) {
      return Optional.of(YELLOW_ORANGE);
    }
    if (BooleanUtils.isTrue(availability.getBackOrder())) {
      log.debug("BACK ORDER Article found");
      return Optional.of(YELLOW);
    }
    Optional<SwissArticleAvailabilityState> state =
        SwissArticleAvailabilityState.fromCode(connectAvailState);
    if (!state.isPresent()) {
      return Optional.of(YELLOW_ORANGE);
    }

    final DateTime cetArrivalDate = availability.getCETArrivalTime();
    final int cetArrivalHourOfDay = cetArrivalDate.getHourOfDay();
    if (cetArrivalDate.toLocalDate().equals(nextWorkingDay.toLocalDate())
        && cetArrivalHourOfDay >= MIDDAY) {
      return Optional.of(YELLOW_GREEN);
    } else if (cetArrivalDate.toLocalDate().isBefore(nextWorkingDay.toLocalDate())
        || (cetArrivalDate.toLocalDate().equals(nextWorkingDay.toLocalDate())
            && cetArrivalHourOfDay < MIDDAY)) {
      return Optional.of(GREEN);
    }

    return Optional.of(YELLOW_ORANGE);
  }
}
