package com.sagag.services.ax.availability.calculator;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.domain.sag.erp.Availability;

public interface ArrivalTimeCalculator {

  boolean calculateArrivalTime(Availability availability,
      Optional<Date> nextWorkingDateForToday, SupportedAffiliate affiliate, String pickupBranchId,
      List<TourTimeDto> tourTimeList, List<WorkingHours> openingHours, String countryName);

  ErpSendMethodEnum sendMethodMode();

  default DateTime resetToOpenTime(Date date, LocalTime openTime) {
    return new DateTime(date).withTime(openTime);
  }

  default boolean isLunchTime(LocalTime time, LocalTime lunchStartTime, LocalTime lunchEndTime) {
    if (Objects.isNull(lunchStartTime) || Objects.isNull(lunchEndTime)) {
      return false;
    }
    return (time.isEqual(lunchStartTime) || time.isAfter(lunchStartTime))
        && time.isBefore(lunchEndTime);
  }

  default boolean isBranchWorkingDay(List<WorkingHours> workingHours, DateTime requestTime) {
    final Optional<WorkingHours> workingHourOpt =
        getWorkingWeekDay(workingHours, requestTime.toDate());
    if (!workingHourOpt.isPresent()) {
      return false;
    }
    final WorkingHours workingHour = workingHourOpt.get();
    return requestTime.withTime(workingHour.getClosingTime()).isAfterNow();
  }

  default Optional<WorkingHours> getWorkingWeekDay(List<WorkingHours> workingHours,
      Date requestTime) {
    final DateTime openingDateTime = new DateTime(requestTime);
    final WeekDay requestWeekDay = WeekDay.findByValue(openingDateTime.getDayOfWeek());
    return CollectionUtils.emptyIfNull(workingHours).parallelStream()
        .filter(workingHour -> workingHour.getWeekDay() == requestWeekDay).findFirst();
  }

}
