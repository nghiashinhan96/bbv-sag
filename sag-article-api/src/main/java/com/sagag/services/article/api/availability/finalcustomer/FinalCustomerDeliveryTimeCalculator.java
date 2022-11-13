package com.sagag.services.article.api.availability.finalcustomer;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.sag.erp.Availability;

import java.util.List;

public interface FinalCustomerDeliveryTimeCalculator {

  /**
   * Calculates article availabilities of final customer.
   *
   * @param article
   * @param deliveryType
   * @param wssDeliveryProfile
   * @param wssMaxAvailabilityDayRange
   *
   * @return article availabilities
   */
  List<Availability> calculateAvailabilitiesForFinalCustomer(ArticleDocDto article,
      String deliveryType, WssDeliveryProfileDto wssDeliveryProfile, Integer wssMaxAvailabilityDayRange);

  /**
   * Get default article availability result
   *
   * @return default article availability result {@link ArticleAvailabilityResult}
   */
  ArticleAvailabilityResult defaultResult();
}
