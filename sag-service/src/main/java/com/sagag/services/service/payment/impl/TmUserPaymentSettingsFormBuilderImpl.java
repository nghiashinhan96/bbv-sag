package com.sagag.services.service.payment.impl;

import com.google.common.collect.Lists;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.profiles.TopmotiveErpProfile;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.hazelcast.api.UserCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@TopmotiveErpProfile
public class TmUserPaymentSettingsFormBuilderImpl
  extends AbstractUserPaymentSettingsFormBuilderImpl {

  @Autowired
  private UserCacheService userCacheService;

  @Override
  public PaymentSettingDto buildUserPaymentSetting(Long userId, boolean salesOnBehalf) {

    final PaymentSettingDto settings = super.buildUserPaymentSetting(userId, salesOnBehalf);

    final UserInfo userInfo = userCacheService.get(userId);
    if (userInfo != null && userInfo.isFinalUserRole()) {
      return settings;
    }
    Optional.ofNullable(userInfo).filter(user -> user.hasCust())
    .map(user -> Lists.newArrayList(user.getCustomer().getStakisDeliveryTypes()))
    .ifPresent(deliveryTypes -> settings.setDeliveryTypes(deliveryTypes));
    return settings;
  }
}
