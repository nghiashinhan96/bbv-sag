package com.sagag.services.article.api.executor;

import com.sagag.services.article.api.price.finalcustomer.FinalCustomerPriceCalculator;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

import java.util.Objects;

public interface AttachedArticleSearchExternalExecutor<R>
    extends IArticleSearchExecutor<AttachedArticleSearchCriteria, R> {

  boolean isCallShoppingCart();

  FinalCustomerPriceCalculator finalCustomerPriceCalculator();

  default void doCalculatePriceForFinalCustomer(ArticleDocDto article,
      ArticleSearchCriteria criteria) {
    final Integer wholesalerOrgId = criteria.getWssOrgId();
    final boolean isWholeSalerHasNetPrice = criteria.isWholeSalerHasNetPrice();
    final boolean isFinalCustHasNetPrice = criteria.isFinalCustomerHasNetPrice();
    final Integer wssMarginGroup = criteria.getFinalCustomerMarginGroup();
    finalCustomerPriceCalculator().handleFinalCustomerNetPrice(article, wholesalerOrgId,
        isWholeSalerHasNetPrice, isFinalCustHasNetPrice, wssMarginGroup)
    .ifPresent(article::setFinalCustomerNetPrice);
  }

  /**
   * Calculates the total attached articles price after get from ERP.
   *
   * <pre>
   * Refer to ticket #2439
   * </pre>
   *
   * @param priceWithArticle the price of attached articles
   */
  default void reCalculateAttachedArticlePrice(final PriceWithArticle priceWithArticle,
      final int quantity) {
    if (Objects.isNull(priceWithArticle) || Objects.isNull(priceWithArticle.getPrice())) {
      return;
    }
    PriceWithArticlePrice priceWithArticlePrice = priceWithArticle.getPrice();
    priceWithArticlePrice.setTotalGrossPrice(priceWithArticlePrice.defaultGrossPrice() * quantity);
    priceWithArticlePrice.setTotalNetPrice(priceWithArticlePrice.defaultNetPrice() * quantity);
  }

}
