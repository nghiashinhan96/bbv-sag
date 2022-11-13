package com.sagag.services.ax.availability.tourtime;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.eshop.repo.api.OpeningDaysCalendarRepository;
import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.erp.TourTimeTable;

@Component
@AxProfile
public class NextWorkingDateHelper {

  @Autowired
  private ArticleExternalService articleExtService;

  @Autowired
  private OpeningDaysCalendarRepository openingDaysCalendarRepo;

  public static DateTime getLastTourTime(final TourTimeTable lastTour,
      final Date nextWorkingDate) {
    if (Objects.nonNull(lastTour)) {
      // Bharani biz ask to get last tour in the tour time table as default next working date time
      return lastTour.getCETStartTime();
    }
    // get default next working date but not correct time when send method is TOUR
    // this networking date as default for PICKUP
    return new DateTime(nextWorkingDate); // implicit convert to CET time
  }

  public Date getNextWorkingDate(String companyName, String branchId, Date requestDate) {
    return articleExtService.getNextWorkingDate(companyName, branchId, requestDate)
        .orElse(requestDate);
  }

  public Date getNextWorkingDayByDays(Date date, String countryName, String companyName,
      String branchId, int nextWorkingDays) {
    return openingDaysCalendarRepo
        .findWorkingDayLaterFrom(date, countryName, companyName, branchId, nextWorkingDays)
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("The brandId %s is missing in OPENING_DAYS_CALENDAR", branchId)));
  }

  public Optional<Date> getNextWorkingDate(Optional<Date> nextWorkingDateForToday,
      List<WorkingHours> openingHours) {
    final WeekDay requestWeekDay = WeekDay.findByValue(DateTime.now().getDayOfWeek());
    final Optional<WorkingHours> openingHourOpt = CollectionUtils.emptyIfNull(openingHours).stream()
        .filter(openingHour -> openingHour.getWeekDay() == requestWeekDay).findFirst();

    if (openingHourOpt.isPresent()) {
      final WorkingHours workingHours = openingHourOpt.get();
      final DateTime closeOfBusinessTime = DateTime.now().withTime(workingHours.getClosingTime());
      if (closeOfBusinessTime.isAfterNow()) {
        return Optional.empty();
      }
    }
    return nextWorkingDateForToday;
  }
}
