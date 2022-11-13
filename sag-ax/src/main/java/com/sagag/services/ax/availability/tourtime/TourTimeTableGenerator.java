package com.sagag.services.ax.availability.tourtime;

import java.util.List;

import org.joda.time.DateTime;

import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.sag.erp.TourTimeTable;

public interface TourTimeTableGenerator {

  /**
   * Generates tour time table data.
   *
   */
  List<TourTimeTable> generate(final DateTime nextWorkingDate,
      final List<TourTimeDto> tourTimeList, final String branchId,
      final SupportedAffiliate affiliate, String countryName);
}
