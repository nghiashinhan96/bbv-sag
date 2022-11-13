package com.sagag.services.article.api.executor;

import com.sagag.services.article.api.availability.finalcustomer.FinalCustomerDeliveryTimeCalculator;
import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.article.api.price.finalcustomer.FinalCustomerPriceCalculator;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;

import java.util.List;

public interface ArticleSearchExternalExecutor
    extends IArticleSearchExecutor<ArticleSearchCriteria, List<ArticleDocDto>> {

  FinalCustomerPriceCalculator finalCustomerPriceCalculator();

  FinalCustomerDeliveryTimeCalculator finalCustomerDeliveryCalculator();

  default void doCalculatePriceForFinalCustomer(List<ArticleDocDto> articles,
      ArticleSearchCriteria criteria) {
    if (!AsyncUpdateMode.PRICE.isValid(criteria)) {
      return;
    }

    final Integer wholesalerOrgId = criteria.getWssOrgId();
    final boolean isWholeSalerHasNetPrice = criteria.isWholeSalerHasNetPrice();
    final boolean isFinalCustHasNetPrice = criteria.isFinalCustomerHasNetPrice();
    final Integer wssMarginGroup = criteria.getFinalCustomerMarginGroup();
    articles.forEach(article -> finalCustomerPriceCalculator()
        .handleFinalCustomerNetPrice(article, wholesalerOrgId, isWholeSalerHasNetPrice,
            isFinalCustHasNetPrice, wssMarginGroup)
        .ifPresent(article::setFinalCustomerNetPrice));

  }

  default void doCalculateAvailabilitiesForFinalCustomer(List<ArticleDocDto> articles,
      ArticleSearchCriteria criteria) {
    if (!AsyncUpdateMode.AVAILABILITY.isValid(criteria)
        || !criteria.isFinalCustomerUser()
        || criteria.getWssDeliveryProfile() == null) {
      return;
    }

    final WssDeliveryProfileDto wssDeliveryProfile = criteria.getWssDeliveryProfile();
    final Integer wssMaxAvailabilityDayRange = criteria.getWssMaxAvailabilityDayRange();
    articles.forEach(article -> article.setAvailabilities(finalCustomerDeliveryCalculator()
        .calculateAvailabilitiesForFinalCustomer(article, criteria.getFinalUserDeliveryType(),
            wssDeliveryProfile, wssMaxAvailabilityDayRange)));
  }

}
