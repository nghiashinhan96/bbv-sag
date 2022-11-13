package com.sagag.services.article.api.price.finalcustomer.impl;

import com.sagag.services.article.api.price.finalcustomer.FinalCustomerPriceCalculator;
import com.sagag.services.article.api.price.finalcustomer.WssMarginValueFinder;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class FinalCustomerPriceCalculatorImpl implements FinalCustomerPriceCalculator {

  private static final int DEFAULT_MARGIN_GROUP = 1;

  @Autowired
  @Qualifier("wssMarginValueByArticleGroupFinderImpl")
  private WssMarginValueFinder wssMarginArticleGroupFinder;

  @Autowired
  @Qualifier("wssMarginValueByBrandFinderImpl")
  private WssMarginValueFinder wssMarginBrandFinder;

  @Override
  public Optional<Double> handleFinalCustomerNetPrice(ArticleDocDto article,
      Integer wholesalerOrgId, boolean isWholeSalerHasNetPrice, boolean isFinalCustomerHasNetPrice,
      Integer wssMarginGroup) {
    if (article == null || !article.hasNetPrice()
        || !isWholeSalerHasNetPrice || !isFinalCustomerHasNetPrice) {
      return Optional.empty();
    }

    final PriceWithArticlePrice articlePrice = article.getPrice().getPrice();
    final Integer brandId = Integer.valueOf(article.getIdDlnr());
    final Double grossPrice = articlePrice.getGrossPrice();
    final Double netPrice = articlePrice.getNetPrice();
    final List<String> articleGroups = article.getArticleGroups();
    final boolean hasGrossPrice = article.hasGrossPrice();

    return Optional.ofNullable(calculateFinalCustomerNetPrice(grossPrice,
        netPrice, articleGroups, brandId, wholesalerOrgId, wssMarginGroup, hasGrossPrice));
  }

  private Double calculateFinalCustomerNetPrice(Double grossPrice, Double sagNetPrice,
      List<String> articleGroups, Integer brandId, Integer wholesalerOrgId,
      Integer marginGroupNumberVal, boolean articleHasGrossPrice) {

    final int marginGroupNumber = marginGroupNumberVal != null
        ? marginGroupNumberVal.intValue() : DEFAULT_MARGIN_GROUP;

    final double articleGroupMargin = wssMarginArticleGroupFinder
        .findMarginValue(wholesalerOrgId, marginGroupNumber, null, articleGroups);

    final double brandMargin = wssMarginBrandFinder
        .findMarginValue(wholesalerOrgId, marginGroupNumber, brandId, Collections.emptyList());

    final double totalMargin = calculateTotalMargin(articleGroupMargin, brandMargin);

    return calculateByMargin(grossPrice, sagNetPrice, totalMargin, articleHasGrossPrice);
  }

  private double calculateByMargin(Double grossPrice, double sagNetPrice, double totalMargin,
      boolean articleHasGrossPrice) {

    double wssNetPrice = sagNetPrice / (1 - (totalMargin * SagConstants.PERCENT));
    // #4648 Price strategy is applied before this, so gross price is Preisemf
    if (!articleHasGrossPrice || Double.compare(grossPrice, wssNetPrice) >= 0) {
      return wssNetPrice;
    }
    return grossPrice;
  }

  private double calculateTotalMargin(double articleMargin, double brandMargin) {
    if (articleMargin > 0 && brandMargin > 0) {
      return NumberUtils.min(articleMargin, brandMargin);
    }
    if (articleMargin > 0) {
      return articleMargin;
    }
    if (brandMargin > 0) {
      return brandMargin;
    }
    return NumberUtils.DOUBLE_ZERO.doubleValue();
  }
}
