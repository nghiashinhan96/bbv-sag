package com.sagag.services.stakis.erp.availability.comparator;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.availability.comparator.AvailabilityArticleComparator;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

@Component
@CzProfile
public class CzArrivalTimeAvailabilityArticleComparator implements AvailabilityArticleComparator {

  @Override
  public List<ArticleDocDto> sortPerfectMatchArticles(List<ArticleDocDto> unsortedArticles) {
    if (CollectionUtils.isEmpty(unsortedArticles)) {
      return Lists.newArrayList();
    }

    final Predicate<ArticleDocDto> arrivalTimePredicate = ArticleDocDto::hasLatestArrivalTime;
    final List<ArticleDocDto> articlesHasArrivalTimeAndSorted = unsortedArticles.stream()
        .filter(arrivalTimePredicate)
        .sorted(this)
        .collect(Collectors.toList());

    final List<ArticleDocDto> articlesHasNoArrivalTime = unsortedArticles.stream()
        .filter(arrivalTimePredicate.negate()).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(articlesHasArrivalTimeAndSorted)) {
      return articlesHasNoArrivalTime;
    }

    return ListUtils.intersection(articlesHasArrivalTimeAndSorted, articlesHasNoArrivalTime);
  }

  @Override
  public int compare(ArticleDocDto art1, ArticleDocDto art2) {
    final Availability lastestAvail1 = art1.getLastestAvailability();
    final Availability lastestAvail2 = art2.getLastestAvailability();

    final DateTime arrivalTime1 =
        lastestAvail1 == null ? null : lastestAvail1.getCETArrivalTime();
    final DateTime arrivalTime2 =
        lastestAvail2 == null ? null : lastestAvail2.getCETArrivalTime();
    if (arrivalTime1 == null && arrivalTime2 == null) {
      return 1;
    }
    if (arrivalTime1 == null || arrivalTime2 == null) {
      return 0;
    }
    return arrivalTime1.compareTo(arrivalTime2);
  }

}
