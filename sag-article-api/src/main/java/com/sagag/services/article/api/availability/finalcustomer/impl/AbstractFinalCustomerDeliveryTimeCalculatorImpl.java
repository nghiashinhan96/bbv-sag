package com.sagag.services.article.api.availability.finalcustomer.impl;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.finalcustomer.FinalCustomerAvailabilityFilter;
import com.sagag.services.article.api.availability.finalcustomer.FinalCustomerDeliveryTimeCalculator;
import com.sagag.services.article.api.utils.ArticleApiUtils;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.sag.erp.Availability;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractFinalCustomerDeliveryTimeCalculatorImpl
    implements FinalCustomerDeliveryTimeCalculator {

  @Autowired
  private List<FinalCustomerAvailabilityFilter> finalCustomerAvailabilityFilters;

  @Override
  public List<Availability> calculateAvailabilitiesForFinalCustomer(ArticleDocDto article,
      String deliveryType, WssDeliveryProfileDto wssDeliveryProfile,
      Integer wssMaxAvailabilityDayRange) {

    final List<Availability> availabilities = article.getAvailabilities();
    if (wssDeliveryProfile == null || isArticleHasNoAvailabilites(article)) {
      return getWssDefaultAvailabilities(article, deliveryType, defaultResult());
    }

    final Availability lastestAvailability = article.getLastestAvailability();
    final Optional<Availability> updatedFinalCustomerAvailabilityOpt =
        finalCustomerAvailabilityFilters.stream()
        .filter(filter ->  filter.sendMethod() == ErpSendMethodEnum.valueOf(deliveryType))
        .findFirst()
        .map(filter -> filter.filterAvailability(lastestAvailability, wssDeliveryProfile,
            wssMaxAvailabilityDayRange));

    if (!updatedFinalCustomerAvailabilityOpt.isPresent()) {
      return getWssDefaultAvailabilities(article, deliveryType, defaultResult());
    }

    final Availability updatedFinalCustomerAvailability = updatedFinalCustomerAvailabilityOpt.get();
    updatedFinalCustomerAvailability
        .setQuantity(availabilities.stream().filter(hasQuantityPredicate())
            .mapToInt(Availability::getQuantity).sum());
    updatedFinalCustomerAvailability.setArticleId(article.getIdSagsys());
    updatedFinalCustomerAvailability.setConExternalSource(
        lastestAvailability.isConExternalSource());
    updatedFinalCustomerAvailability.setExternalSource(
        lastestAvailability.isExternalSource());
    updatedFinalCustomerAvailability.setVenExternalSource(
        lastestAvailability.isVenExternalSource());

    lastestAvailability.setQuantity(availabilities.stream().filter(hasQuantityPredicate())
        .mapToInt(Availability::getQuantity).sum());
    lastestAvailability.setArrivalTime(updatedFinalCustomerAvailability.getArrivalTime());
    lastestAvailability.setSendMethodCode(updatedFinalCustomerAvailability.getSendMethodCode());
    return Lists.newArrayList(updatedFinalCustomerAvailability);
  }

  private Predicate<? super Availability> hasQuantityPredicate() {
    return avail -> Objects.nonNull(avail.getQuantity());
  }

  protected List<Availability> getWssDefaultAvailabilities(ArticleDocDto article,
      String deliveryType, ArticleAvailabilityResult defaultResult) {
    Availability latestAvailability = article.getLastestAvailability();
    String arrivalTime = latestAvailability.getArrivalTime();
    List<Availability> defaultAvailabilities =
        ArticleApiUtils.defaultAvailabilities(article, deliveryType, defaultResult);
    defaultAvailabilities.stream()
        .forEach(availability -> availability.setArrivalTime(arrivalTime));

    return defaultAvailabilities;
  }

  protected abstract boolean isArticleHasNoAvailabilites(ArticleDocDto article);

}
