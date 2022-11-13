package com.sagag.services.ax.availability.externalvendor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.externalvendor.ExternalStockFinder;
import com.sagag.services.article.api.domain.vendor.ExternalStockSearchCriteria;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ExternalVendorSearchExecutor;
import com.sagag.services.article.api.executor.callable.ChunkedRequestProcessor;
import com.sagag.services.ax.domain.vendor.ExternalStockInfo;
import com.sagag.services.ax.domain.vendor.VenExternalStockSearchCriteria;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalStock;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

@Component
@AxProfile
public class VenExternalStockFinder implements ExternalStockFinder,
  ChunkedRequestProcessor<ExternalStockInfo> {

  @Autowired
  private ExternalVendorStockFinder externalVendorStockFinder;

  @Autowired
  private ExternalVendorSearchExecutor externalVendorSearchExecutor;

  @Autowired
  private PotentialExternalArticlesFilter potentialExternalArticlesFilter;

  @Autowired
  private AppProperties appProps;

  @Override
  public List<ArticleDocDto> findStocks(List<ArticleDocDto> articles,
      ExternalStockSearchCriteria stockSearchCriteria) {

    final VenExternalStockSearchCriteria criteria =
        VenExternalStockSearchCriteria.class.cast(stockSearchCriteria);
    final String companyName = criteria.getCompanyName();
    final List<ExternalVendorDto> venExternalVendor =
        CollectionUtils.emptyIfNull(criteria.getExternalVendors()).stream()
            .filter(vendor -> AxAvailabilityType.VEN.name().equals(vendor.getAvailabilityTypeId()))
            .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(venExternalVendor)) {
      return articles;
    }

    final List<ArticleDocDto> canBeSuppliedByExternalVendorArticles =
        potentialExternalArticlesFilter.filterArticlesCouldBeSuppliedByExternalVendor(articles,
            venExternalVendor);
    if (CollectionUtils.isEmpty(canBeSuppliedByExternalVendorArticles)) {
      return articles;
    }

    final List<String> canBeSuppliedByExternalVendorArticleIds =
      canBeSuppliedByExternalVendorArticles.stream().map(ArticleDocDto::getIdSagsys)
        .collect(Collectors.toList());
    final List<VendorDto> vendors = externalVendorSearchExecutor.execute(companyName,
      canBeSuppliedByExternalVendorArticleIds);

    final List<ExternalStockInfo> externalStocks = partition(vendors).stream()
        .map(ArrayList::new)
        .map(partition -> externalVendorStockFinder.findExternalStocks(
            criteria.toArticleSearchCriteria(), partition, canBeSuppliedByExternalVendorArticles))
        .flatMap(List::stream).collect(Collectors.toList());

    articles.stream().forEach(article -> updateStock(article, externalStocks));
    return articles;
  }

  private void updateStock(ArticleDocDto article, List<ExternalStockInfo> externalStocks) {
    String articleId = article.getIdSagsys();
    externalStocks.stream().filter(item -> articleId.equals(item.getArticleId())).findAny()
        .ifPresent(stock -> article.setExternalStock(toExternalStock(stock)));
  }

  private ExternalStock toExternalStock(ExternalStockInfo stock) {
    return ExternalStock.builder().articleId(stock.getArticleId()).vendorId(stock.getVendorId())
        .build();
  }

  @Override
  public int maxRequestSize() {
    return appProps.getErpConfig().getMaxRequestSize();
  }
}
