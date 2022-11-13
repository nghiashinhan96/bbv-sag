package com.sagag.services.ax.availability.finalcustomer.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.finalcustomer.impl.AbstractFinalCustomerDeliveryTimeCalculatorImpl;
import com.sagag.services.ax.availability.processor.AxArticleAvailabilityProcessor;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

@Component
@AxProfile
public class AxFinalCustomerDeliveryTimeCalculatorImpl
    extends AbstractFinalCustomerDeliveryTimeCalculatorImpl {

  @Autowired
  private AxArticleAvailabilityProcessor availabilityProcessor;

  @Override
  public ArticleAvailabilityResult defaultResult() {
    return availabilityProcessor.getDefaultResultForNoPrice();
  }

  @Override
  protected boolean isArticleHasNoAvailabilites(ArticleDocDto article) {
    final List<Availability> availabilities = article.getAvailabilities();
    return CollectionUtils.isEmpty(availabilities)
        || availabilities.stream().anyMatch(avail -> BooleanUtils.isNotFalse(avail.getBackOrder()));
  }

}
