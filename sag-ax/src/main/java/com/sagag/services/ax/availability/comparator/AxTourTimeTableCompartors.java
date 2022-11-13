package com.sagag.services.ax.availability.comparator;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.sagag.services.ax.availability.tourtime.AxTourTimeTableUtils;
import com.sagag.services.domain.sag.erp.TourTimeTable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AxTourTimeTableCompartors {

  public static Comparator<TourTimeTable> defaultComparator(String branchId) {
    return  Comparator
        .comparing(TourTimeTable::getCETStartTime, sortByTourDateTimeAsc())
        .thenComparing(item -> AxTourTimeTableUtils.getBranchIdFromTourName(item.getTourName()),
            sortByDefaultBranch(branchId));
  }

  private static Comparator<DateTime> sortByTourDateTimeAsc() {
    return (startTime1, startTime2) -> {
      if (startTime1 == null || startTime2 == null) {
        return -1;
      }
      return startTime1.compareTo(startTime2);
    };
  }

  private static Comparator<String> sortByDefaultBranch(String defaultBranch) {
    return (branch1, branch2) -> {
      if (StringUtils.isBlank(branch1) || StringUtils.isBlank(branch2)) {
        return -1;
      }
      return Boolean.compare(defaultBranch.equals(branch2), defaultBranch.equals(branch1));
    };
  }

}
