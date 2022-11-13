package com.sagag.services.article.api.price;

import com.sagag.services.domain.sag.erp.PriceWithArticle;

import java.util.Optional;

@FunctionalInterface
public interface PriceDiscountPromotionCalculator {

  /**
   * Calculates price discount promotion.
   * @param priceWithArticle
   * @param quantity
   */
  void calculatorPriceDiscountPromotion(PriceWithArticle priceWithArticle,
      Optional<Integer> quantity);
}
