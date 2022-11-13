package com.sagag.services.ax.availability.externalvendor;

import java.util.List;

import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;

public interface ExternalVendorAvailabilityFilter {

  /**
   * Returns the availabilities type to calculation.
   */
  List<AxAvailabilityType> availabilityTypes();

  /**
   * Filters availability for articles provided by external vendors.
   *
   * @param articles
   * @param artCriteria
   * @param availCriteria
   * @param axVendors
   * @param countryName
   * @return
   */
  List<ArticleDocDto> filter(List<ArticleDocDto> articles, ArticleSearchCriteria artCriteria,
      AdditionalArticleAvailabilityCriteria availCriteria, List<VendorDto> axVendors,
      String countryName);
}
