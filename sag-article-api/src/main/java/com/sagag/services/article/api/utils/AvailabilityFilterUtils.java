package com.sagag.services.article.api.utils;

import com.sagag.services.domain.sag.erp.Availability;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.Comparator;

@UtilityClass
public class AvailabilityFilterUtils {

  public static Comparator<Availability> sortByArrivalTimeIfExist() {
    return (a1, a2) -> {
      if (StringUtils.isBlank(a1.getArrivalTime()) || StringUtils.isBlank(a2.getArrivalTime())) {
        return 1;
      }
      final DateTime arrivalTime1 = a1.getCETArrivalTime();
      final DateTime arrivalTime2 = a2.getCETArrivalTime();
      return arrivalTime1.compareTo(arrivalTime2);
    };
  }
}
