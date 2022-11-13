package com.sagag.services.ax.availability.tourtime;

import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AxTourTimeTableUtils {

  private static final int DEFAULT_HOUR = 0;

  private static final int DEFAULT_MINUTE = 0;

  private static final int START_INDEX_TOUR_TIME = 2;

  private static final int START_INDEX_BRANCH_ID = 0;

  private static final int START_INDEX_OF_HOUR = 0;

  private static final int END_INDEX_OF_HOUR = 2;

  private static final char UNDERSCORE_DELIMITER = SagConstants.UNDERSCORE_CHAR;

  private static final int LENGTH_OF_TOUR_NAME = 3;


  private static Optional<Integer> getCETHourFromTourName(final String tourName) {
    final String tourTime = getTourTimeFromTourName(tourName);
    if (StringUtils.isBlank(tourTime) || !StringUtils.isNumeric(tourTime)) {
      return Optional.empty();
    }
    return Optional.of(NumberUtils.toInt(
        StringUtils.substring(tourTime, START_INDEX_OF_HOUR, END_INDEX_OF_HOUR)));
  }

  private static Optional<Integer> getCETMinuteFromTourName(final String tourName) {
    final String tourTime = getTourTimeFromTourName(tourName);
    if (StringUtils.isBlank(tourTime) || !StringUtils.isNumeric(tourTime)) {
      return Optional.empty();
    }
    return Optional.of(NumberUtils.toInt(StringUtils.substring(tourTime, END_INDEX_OF_HOUR)));
  }

  private static String getTourTimeFromTourName(final String tourName) {
    return getStringValueFromTourName(tourName, START_INDEX_TOUR_TIME);
  }

  public static String getBranchIdFromTourName(final String tourName) {
    return getStringValueFromTourName(tourName, START_INDEX_BRANCH_ID);
  }

  private static String getStringValueFromTourName(final String tourName, final int index) {
    if (StringUtils.isBlank(tourName) || index < START_INDEX_BRANCH_ID) {
      return StringUtils.EMPTY;
    }
    final String[] strs = ArrayUtils.nullToEmpty(StringUtils.split(tourName, UNDERSCORE_DELIMITER));
    if (ArrayUtils.isEmpty(strs) || ArrayUtils.getLength(strs) != LENGTH_OF_TOUR_NAME) {
      return StringUtils.EMPTY;
    }
    return strs[index];
  }

  public static DateTime createDateTimeFromTourName(final DateTime orgDate,
      final String tourName) {
    final Optional<Integer> hourCETOpt = getCETHourFromTourName(tourName);
    final Optional<Integer> minuteCETOpt = getCETMinuteFromTourName(tourName);
    return orgDate.withHourOfDay(hourCETOpt.orElse(DEFAULT_HOUR))
        .withMinuteOfHour(minuteCETOpt.orElse(DEFAULT_MINUTE))
        .withSecondOfMinute(NumberUtils.INTEGER_ZERO);
  }
}
