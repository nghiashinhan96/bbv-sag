package com.sagag.services.article.api.availability.externalvendor;

import com.sagag.services.article.api.domain.vendor.ExternalStockSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;

import java.util.List;

public interface ExternalStockFinder {

  /**
   * Finds articles contain external stocks.
   *
   * @param articles
   * @param criteria
   * @return the filtered articles contain external stock
   */
  List<ArticleDocDto> findStocks(List<ArticleDocDto> articles,
      ExternalStockSearchCriteria criteria);
}
