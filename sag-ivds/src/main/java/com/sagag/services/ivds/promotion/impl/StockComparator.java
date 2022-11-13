package com.sagag.services.ivds.promotion.impl;

import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.ivds.promotion.ArticleComparator;

import java.util.Comparator;
import java.util.Objects;

public final class StockComparator implements ArticleComparator {

  private static final StockComparator comparator = new StockComparator();

  private StockComparator() {}

  public static ArticleComparator getInstance() {
    return comparator;
  }

  @Override
  public int compare(ArticleDocDto art1, ArticleDocDto art2) {
    return Comparator.comparing(ArticleDocDto::getStock, sortByLocalStock())
        .thenComparing(ArticleDocDto::getStockNr, sortByStockNr()).compare(art1, art2);
  }

  private Comparator<ArticleStock> sortByLocalStock() {
    return (stock1, stock2) -> Boolean.compare(
        Objects.nonNull(stock2) && stock2.getStockAmount() >= TyreConstants.MENGE_OF_TYRES_CAR_VEHICLE,
        Objects.nonNull(stock1) && stock1.getStockAmount() >= TyreConstants.MENGE_OF_TYRES_CAR_VEHICLE);
  }

  private Comparator<Double> sortByStockNr() {
    return (a, b) -> b.compareTo(a);
  }
}
