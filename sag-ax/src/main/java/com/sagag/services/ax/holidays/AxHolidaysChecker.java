package com.sagag.services.ax.holidays;

import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysAvailabilityInfo;

/**
 * Interface for Holidays Checker.
 *
 */
public interface AxHolidaysChecker {

  boolean isNonWorkingDay(OpeningDaysAvailabilityInfo availabilityInfo);
}