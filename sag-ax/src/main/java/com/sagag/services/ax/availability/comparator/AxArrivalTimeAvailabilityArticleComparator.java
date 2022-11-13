package com.sagag.services.ax.availability.comparator;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.comparator.AvailabilityArticleComparator;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

@Component
@AxProfile
public class AxArrivalTimeAvailabilityArticleComparator implements AvailabilityArticleComparator {

  @Override
  public int compare(ArticleDocDto art1, ArticleDocDto art2) {

    final Availability lastestAvail1 = art1.getLastestAvailability();
    final Availability lastestAvail2 = art2.getLastestAvailability();

    final DateTime arrivalTime1 =
        lastestAvail1 == null ? null : lastestAvail1.getCETArrivalTime();
    final DateTime arrivalTime2 =
        lastestAvail2 == null ? null : lastestAvail2.getCETArrivalTime();
    if (arrivalTime1 == null && arrivalTime2 == null) {
      return 0;
    }
    if (arrivalTime1 == null) {
      return -1;
    }
    if (arrivalTime2 == null) {
      return 1;
    }
    return arrivalTime1.compareTo(arrivalTime2);
  }

}
