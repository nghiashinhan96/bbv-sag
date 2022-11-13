package com.sagag.services.article.api.price.finalcustomer;

import com.sagag.services.domain.article.ArticleDocDto;

import java.util.Optional;

public interface FinalCustomerPriceCalculator {


  /**
   * Calculates net price for final customer
   *
   * @param article
   * @param wholesalerOrgId
   * @param isWholeSalerHasNetPrice
   * @param isFinalCustomerHasNetPrice
   * @param wssMarginGroup
   * @return net price for final customer
   */
  Optional<Double> handleFinalCustomerNetPrice(ArticleDocDto article, Integer wholesalerOrgId,
      boolean isWholeSalerHasNetPrice, boolean isFinalCustomerHasNetPrice, Integer wssMarginGroup);

}
