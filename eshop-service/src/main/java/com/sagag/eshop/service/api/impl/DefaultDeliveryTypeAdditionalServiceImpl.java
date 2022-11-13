package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.api.DeliveryTypeAdditionalService;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;

public class DefaultDeliveryTypeAdditionalServiceImpl implements DeliveryTypeAdditionalService {

  @Override
  public void additionalHandleForDeliveryType(String orgCode, PaymentSettingDto paymentSettingDto) {
    // Do nothing by default
  }

}
