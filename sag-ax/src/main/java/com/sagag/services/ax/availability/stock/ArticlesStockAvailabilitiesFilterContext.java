package com.sagag.services.ax.availability.stock;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;

import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.availability.StockAvailabilitiesFilter;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;

public abstract class ArticlesStockAvailabilitiesFilterContext {

  @Autowired
  protected List<StockAvailabilitiesFilter> stockAvailabilitiesFilters;

  public void doFilterStockAvailabilities(final List<ArticleDocDto> filteredArticles,
      final List<ArticleDocDto> articles, final String countryName,
      final ArticleSearchCriteria criteria,
      final AdditionalArticleAvailabilityCriteria availCriteria, final List<VendorDto> axVendors) {
    stockAvailabilitiesFilters.stream().forEach(stockAvailabilitiesFilterConsumer(filteredArticles,
        articles, countryName, criteria, availCriteria, axVendors));
  }

  private Consumer<StockAvailabilitiesFilter> stockAvailabilitiesFilterConsumer(
      List<ArticleDocDto> filteredArticles, List<ArticleDocDto> articles, String countryName,
      ArticleSearchCriteria criteria, AdditionalArticleAvailabilityCriteria availCriteria,
      List<VendorDto> axVendors) {
    return filter -> filteredArticles
        .addAll(filter.doFilterAvailabilities(filter.extractBeforeCalculation(articles),
            countryName, criteria, availCriteria, axVendors));
  }

}
