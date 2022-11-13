package com.sagag.services.ax.availability.stock;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.availability.AvailabilityFilter;
import com.sagag.services.article.api.availability.StockAvailabilitiesFilter;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.availability.externalvendor.ConExternalVendorAvailabilityFilter;
import com.sagag.services.ax.availability.filter.AxAvailabilityFilter;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.extern.slf4j.Slf4j;

@Component
@DynamicAxProfile
@Slf4j
@Order(1)
public class ArticleIsOnStockAvailabilitiesFilter implements StockAvailabilitiesFilter {

  @Autowired
  private ConExternalVendorAvailabilityFilter conExternalVendorAvailabilityFilter;

  @Autowired
  private AxAvailabilityFilter availabilityFilter;

  @Override
  @LogExecutionTime
  public List<ArticleDocDto> doFilterAvailabilities(List<ArticleDocDto> articles,
      String countryName, ArticleSearchCriteria criteria,
      AdditionalArticleAvailabilityCriteria availCriteria, List<VendorDto> axVendors) {
    log.debug("Filtering articles has stock");

    if (CollectionUtils.isEmpty(articles)) {
      log.debug("No articles with stock");
      return Collections.emptyList();
    }

    filterAvailabilitiesForArticles(articles, countryName,
        criteria, availCriteria);

    if (criteria.isDropShipment()) {
      List<ArticleDocDto> articlesWithConVendorAvail = conExternalVendorAvailabilityFilter
          .filter(articles, criteria, availCriteria, axVendors, countryName);

      AxArticleUtils.updateArticlesAvailability(articles, articlesWithConVendorAvail);
    }
    return articles;
  }

  @Override
  public Predicate<ArticleDocDto> extractFilter() {
    return ArticleDocDto::isOnStock;
  }

  @Override
  public AvailabilityFilter availabilityFilter() {
    return availabilityFilter;
  }
}
