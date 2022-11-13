package com.sagag.services.article.api.availability;

import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@FunctionalInterface
public interface AvailabilityFilter {

  /**
   * Returns filtered availabilities of article.
   *
   * @param article
   * @param artCriteria
   * @param tourTimeList
   * @param openingHours
   * @param countryName
   * @return the list of availabilities
   */
  List<Availability> filterAvailabilities(ArticleDocDto article, ArticleSearchCriteria artCriteria,
      List<TourTimeDto> tourTimeList, List<WorkingHours> openingHours, String countryName);

  default Predicate<Availability> backOrderTruePredicate() {
    return availability -> Boolean.TRUE.equals(availability.getBackOrder());
  }

  default BiPredicate<ArticleSearchCriteria, ArticleDocDto> noPricePredicate() {
    return (criteria, article) -> criteria.isUpdatePrice() && !article.hasGrossPrice();
  }

  default BiPredicate<ArticleSearchCriteria, ArticleDocDto> noAvailabilitiesPredicate() {
    return (criteria, article) -> !article.hasAvailabilities();
  }
}
