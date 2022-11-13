package com.sagag.services.ax.availability.wholesaler;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.WssDeliveryTimeCalculator;
import com.sagag.services.article.api.availability.finalcustomer.FinalCustomerAvailabilityFilter;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.sag.erp.Availability;

@Component
@AxProfile
public class WssDeliveryTimeCalculatorImpl implements WssDeliveryTimeCalculator {

  @Autowired
  private List<FinalCustomerAvailabilityFilter> finalCustomerAvailabilityFilters;

  @Override
  public List<Availability> calculateAvailabilitiesForFinalCustomer(ArticleDocDto article,
      String deliveryType, WssDeliveryProfileDto wssDeliveryProfile,
      ArticleAvailabilityResult defaultResult, Integer wssMaxAvailabilityDayRange) {

    final List<Availability> availabilities = article.getAvailabilities();
    if (wssDeliveryProfile == null
        || CollectionUtils.isEmpty(availabilities)
        || availabilities.stream().anyMatch(Availability::isBackOrderTrue)) {
      return getWssDefaultAvailabilities(article, deliveryType, defaultResult);
    }

    final Availability lastestAvailability = article.getLastestAvailability();
    final Optional<Availability> updatedFinalCustomerAvailabilityOpt =
        finalCustomerAvailabilityFilters.stream()
        .filter(filter ->  filter.sendMethod() == ErpSendMethodEnum.valueOf(deliveryType))
        .findFirst().map(filter -> filter.filterAvailability(
            lastestAvailability, wssDeliveryProfile, wssMaxAvailabilityDayRange));

    if (!updatedFinalCustomerAvailabilityOpt.isPresent()) {
      return getWssDefaultAvailabilities(article, deliveryType, defaultResult);
    }

    final Availability updatedFinalCustomerAvailability = updatedFinalCustomerAvailabilityOpt.get();
    updatedFinalCustomerAvailability.setQuantity(availabilities.stream()
        .mapToInt(Availability::getQuantity).sum());
    updatedFinalCustomerAvailability.setArticleId(article.getIdSagsys());
    updatedFinalCustomerAvailability.setConExternalSource(
        lastestAvailability.isConExternalSource());
    updatedFinalCustomerAvailability.setExternalSource(lastestAvailability.isExternalSource());
    updatedFinalCustomerAvailability.setVenExternalSource(
        lastestAvailability.isVenExternalSource());

    lastestAvailability.setQuantity(availabilities.stream()
        .mapToInt(Availability::getQuantity).sum());
    lastestAvailability.setArrivalTime(updatedFinalCustomerAvailability.getArrivalTime());
    lastestAvailability.setSendMethodCode(updatedFinalCustomerAvailability.getSendMethodCode());
    return Lists.newArrayList(updatedFinalCustomerAvailability);
  }

  private List<Availability> getWssDefaultAvailabilities(ArticleDocDto article, String deliveryType,
      ArticleAvailabilityResult defaultResult) {
    Availability latestAvailability = article.getLastestAvailability();
    String arrivalTime = latestAvailability.getArrivalTime();
    List<Availability> defaultAvailabilities =
        AxArticleUtils.defaultAvailabilities(article, deliveryType, defaultResult);
    defaultAvailabilities.stream().forEach(item -> item.setArrivalTime(arrivalTime));
    return defaultAvailabilities;
  }

}
