package com.sagag.eshop.service.api;

import com.sagag.services.domain.eshop.dto.PaymentSettingDto;

public interface DeliveryTypeAdditionalService {

  public void additionalHandleForDeliveryType(String orgCode, PaymentSettingDto paymentSettingDto);

}
