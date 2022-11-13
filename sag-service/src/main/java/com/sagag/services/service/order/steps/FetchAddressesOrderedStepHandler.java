package com.sagag.services.service.order.steps;

import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Slf4j
public class FetchAddressesOrderedStepHandler {

  private static final String LOG_INFO = "Fetching the list of addresses to make order request "
      + "with order condition = {}";

  private static final String LOG_WARN = "BillingAddress or deliveryAddress for the user id = {} "
      + "is empty in Eshop basket context, use default user setting address instead ";

  @Autowired
  private UserSettingsRepository userSettingRepo;

  public void handle(final Long userId, final OrderConditionContextBody orderCondition) {
    log.debug(LOG_INFO, orderCondition);
    Assert.notNull(orderCondition, "The given eshop context must not be null");
    if (orderCondition.hasBillingAddr() && orderCondition.hasDeliveryAddr()) {
      return;
    }
    final UserSettings userSettings = userSettingRepo.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("Not found any setting for this userid = %d ", userId)));
    log.warn(LOG_WARN, userId);
    orderCondition.setDeliveryAddressId(userSettings.getDeliveryAddressId());
    orderCondition.setBillingAddressId(userSettings.getBillingAddressId());
  }

}
