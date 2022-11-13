package com.sagag.services.stakis.erp.availability.finalcustomer.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.finalcustomer.impl.AbstractFinalCustomerDeliveryTimeCalculatorImpl;
import com.sagag.services.article.api.utils.ArticleApiUtils;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.stakis.erp.availability.CzArticleAvailabilityProcessor;
import com.sagag.services.stakis.erp.enums.StakisArticleAvailabilityState;

@Component
@CzProfile
public class StakisFinalCustomerDeliveryTimeCalculatorImpl
    extends AbstractFinalCustomerDeliveryTimeCalculatorImpl {

  @Autowired
  private CzArticleAvailabilityProcessor czArtAvailabilityProcessor;

  @Override
  public ArticleAvailabilityResult defaultResult() {
    return czArtAvailabilityProcessor.getDefaultResultForNoPrice();
  }

  @Override
  protected boolean isArticleHasNoAvailabilites(ArticleDocDto article) {
    final List<Availability> availabilities = article.getAvailabilities();
    final Availability lastestAvailability = article.getLastestAvailability();
    return CollectionUtils.isEmpty(availabilities)
        || availabilities.stream()
            .allMatch(availability -> StakisArticleAvailabilityState.BLACK.getCode() == availability
                .getAvailState()
                || StakisArticleAvailabilityState.RED.getCode() == availability.getAvailState())
        || StringUtils.isBlank(lastestAvailability.getRawArrivalTime());
  }

  @Override
  protected List<Availability> getWssDefaultAvailabilities(ArticleDocDto article,
      String deliveryType, ArticleAvailabilityResult defaultResult) {
    return ArticleApiUtils.defaultAvailabilities(article, deliveryType, defaultResult);
  }
}
