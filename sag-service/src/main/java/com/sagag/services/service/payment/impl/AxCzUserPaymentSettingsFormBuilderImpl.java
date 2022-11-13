package com.sagag.services.service.payment.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.common.profiles.AxCzProfile;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.hazelcast.api.TourTimeCacheService;
import com.sagag.services.hazelcast.api.UserCacheService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AxCzProfile
public class AxCzUserPaymentSettingsFormBuilderImpl
  extends AbstractUserPaymentSettingsFormBuilderImpl {

  private static final String DELIVERY_TYPE_TOUR = "TOUR";

  private static final String AX_PICK_UP_TYPE = "ABH";

  @Autowired
  private UserCacheService userCacheService;

  @Autowired
  private TourTimeCacheService tourTimeCacheService;

  @Override
  public PaymentSettingDto buildUserPaymentSetting(Long userId, boolean salesOnBehalf) {
    PaymentSettingDto paymentSetting = super.buildUserPaymentSetting(userId, salesOnBehalf);
    final UserInfo user = userCacheService.get(userId);

    if (user != null && user.isFinalUserRole()) {
      return paymentSetting;
    }
    if (userHasOnlyDeliveryTypeABH(user)) {
      thenIgnoreTourType(paymentSetting);
    }

    return paymentSetting;
  }

  private void thenIgnoreTourType(PaymentSettingDto paymentSetting) {
    List<DeliveryTypeDto> deliveryTypeWithoutTour = paymentSetting
        .getDeliveryTypes().stream().filter(deliveryType -> !StringUtils
            .equalsIgnoreCase(deliveryType.getDescCode(), DELIVERY_TYPE_TOUR))
        .collect(Collectors.toList());
    paymentSetting.setDeliveryTypes(deliveryTypeWithoutTour);
  }

  private boolean userHasOnlyDeliveryTypeABH(UserInfo user) {
    if (Objects.isNull(user) || user.isSalesUser()) {
      return false;
    }
    String custNr = user.getCustNr();
    List<TourTimeDto> tourTimes = tourTimeCacheService.searchTourTimesByCustNr(custNr);
    return StringUtils.equalsIgnoreCase(user.getCustomer().getAxSendMethod(), AX_PICK_UP_TYPE)
        && CollectionUtils.isEmpty(tourTimes);
  }

}
