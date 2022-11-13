package com.sagag.services.ax.availability;

import java.util.List;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.sag.erp.Availability;

public interface WssDeliveryTimeCalculator {

  /**
   * Calculates availabilities for final customer by wholesaler settings.
   *
   * @param article
   * @param deliveryType
   * @param wssDeliveryProfile
   * @param defaultResult
   * @param wssMaxAvailabilityDayRange
   * @return the list of availabilities
   */
  List<Availability> calculateAvailabilitiesForFinalCustomer(ArticleDocDto article,
      String deliveryType, WssDeliveryProfileDto wssDeliveryProfile,
      ArticleAvailabilityResult defaultResult, Integer wssMaxAvailabilityDayRange);
}
