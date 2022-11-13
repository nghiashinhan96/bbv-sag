package com.sagag.services.service.user.handler;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.profiles.WintProfile;
import com.sagag.services.domain.eshop.dto.CourierDto;
import com.sagag.services.domain.eshop.dto.OrderLocation;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.service.order.location.CourierBuilder;
import com.sagag.services.service.order.location.OrderLocationBuilder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@WintProfile
@Component
@Slf4j
public class SbEshopSelfManageDataHandler extends EshopSelfManageDataHandler {

  @Autowired
  private OrderLocationBuilder orderLocationBuilder;

  @Autowired
  private CourierBuilder courierBuilder;

  @Override
  public PaymentSettingDto getPaymentSetting(UserInfo user) {
    log.debug("Start get payment setting");
    final PaymentSettingDto paymentSettings =
        userPaymentSettingsFormBuilder.buildUserPaymentSetting(user.getId(), user.isSaleOnBehalf());
    fillPaymentAddresses(paymentSettings, user);
    List<CourierDto> courierServices =  user.isSalesNotOnBehalf() ? Collections.emptyList()
        : courierBuilder.buildCourierServices(user.getCompanyName());

    List<OrderLocation> orderLocations = orderLocationBuilder.buildOrderLocations(
        user.getCustomer().getGrantedBranches(), user.getCompanyName(),
        paymentSettings.getDeliveryTypes(), paymentSettings.getPaymentMethods(), courierServices);
    paymentSettings.setOrderLocations(orderLocations);

    return paymentSettings;
  }

}
