package com.sagag.services.ax.availability.externalvendor;

import java.util.List;

import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.domain.vendor.ExternalStockInfo;
import com.sagag.services.domain.article.ArticleDocDto;

public interface ExternalVendorStockFinder {

  /**
   * Returns external stocks.
   *
   * @param artCriteria
   * @param axVendors
   * @param articles
   * @return
   */
  List<ExternalStockInfo> findExternalStocks(ArticleSearchCriteria artCriteria,
      List<VendorDto> axVendors, List<ArticleDocDto> articles);
}
