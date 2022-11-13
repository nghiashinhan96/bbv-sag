package com.sagag.services.ax.translator.wint;

import org.springframework.stereotype.Component;

import com.sagag.services.ax.enums.AxPaymentMethod;
import com.sagag.services.ax.translator.PaymentMethodTranslator;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.WintProfile;

@Component
@WintProfile
public class WintPaymentMethodTranslator extends PaymentMethodTranslator {

  @Override
  public void afterPropertiesSet() throws Exception {
    this.getPaymentMethodMap().putIfAbsent(PaymentMethodType.CASH,
        AxPaymentMethod.CASHENUM);
    this.getPaymentMethodMap().putIfAbsent(PaymentMethodType.WHOLESALE,
        AxPaymentMethod.DIRECTINVOICEENUM);
  }


}
