package com.sagag.services.article.api.availability;

import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface StockAvailabilitiesFilter {

  /**
   * Filters availabilities of the list of articles by criteria.
   *
   * @param articles
   * @param countryName
   * @param criteria
   * @param availCriteria
   * @param axVendors
   * @return
   */
  List<ArticleDocDto> doFilterAvailabilities(List<ArticleDocDto> articles, String countryName,
      ArticleSearchCriteria criteria, AdditionalArticleAvailabilityCriteria availCriteria,
      List<VendorDto> axVendors);

  /**
   * Returns implementation of availability filter.
   *
   */
  AvailabilityFilter availabilityFilter();

  /**
   * Returns the extract predicate for that availability.
   *
   * @param articles the original list of articles
   * @return the extracted articles
   */
  Predicate<ArticleDocDto> extractFilter();

  /**
   * Extracts articles before availability calculation.
   *
   * @param articles the original list of articles
   * @return the extracted articles
   */
  default List<ArticleDocDto> extractBeforeCalculation(List<ArticleDocDto> articles) {
    if (CollectionUtils.isEmpty(articles)) {
      return Collections.emptyList();
    }
    return articles.stream().filter(extractFilter()).collect(Collectors.toList());
  }

  /**
   * Filters availabilities for articles.
   *
   * @param articles
   * @param countryName
   * @param criteria
   * @param availCriteria
   */
  default void filterAvailabilitiesForArticles(List<ArticleDocDto> articles, String countryName,
      ArticleSearchCriteria criteria, AdditionalArticleAvailabilityCriteria availCriteria) {
    for (ArticleDocDto article : articles) {
      article.setAvailabilities(availabilityFilter().filterAvailabilities(article, criteria,
          availCriteria.getTourTimeList(), availCriteria.getOpeningHours(), countryName));
    }
  }

}
