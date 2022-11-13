package com.sagag.services.ax.availability.tourtime.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.ax.availability.comparator.AxTourTimeTableCompartors;
import com.sagag.services.ax.availability.tourtime.AxTourTimeTableUtils;
import com.sagag.services.ax.availability.tourtime.TourTimeTableGenerator;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.sag.erp.TourTimeTable;

@Component
@AxProfile
public class AxDefaultTourTimeTableGenerator implements TourTimeTableGenerator {

  @Override
  public List<TourTimeTable> generate(DateTime arrivalTime, List<TourTimeDto> tourTimeList,
      String branchId, SupportedAffiliate affiliate, String countryName) {
    final List<TourTimeTable> tourTimeTableList = new ArrayList<>();
    String tourName;
    DateTime tourDateTime;
    for (TourTimeDto tourTime : CollectionUtils.emptyIfNull(tourTimeList)) {
      tourName = tourTime.getTourName();
      tourDateTime = AxTourTimeTableUtils.createDateTimeFromTourName(arrivalTime, tourName);
      tourTimeTableList.add(new TourTimeTable(
          StringUtils.defaultString(tourName), DateUtils.getUTCStr(tourDateTime),
          tourTime.getTourDays(), tourTime.getTourDepartureTime(), tourTime.getCutOffMinutes()));
    }

    Collections.sort(tourTimeTableList, AxTourTimeTableCompartors.defaultComparator(branchId));
    return tourTimeTableList;
  }

}
