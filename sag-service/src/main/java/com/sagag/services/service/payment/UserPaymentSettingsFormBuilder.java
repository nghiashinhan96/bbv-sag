package com.sagag.services.service.payment;

import com.sagag.services.domain.eshop.dto.PaymentSettingDto;

public interface UserPaymentSettingsFormBuilder {

  PaymentSettingDto buildUserPaymentSetting(final Long userId, boolean salesOnBehalf);

}
