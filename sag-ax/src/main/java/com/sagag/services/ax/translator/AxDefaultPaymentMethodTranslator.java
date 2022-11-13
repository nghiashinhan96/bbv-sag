package com.sagag.services.ax.translator;

import org.springframework.stereotype.Component;

import com.sagag.services.ax.enums.AxPaymentMethod;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.DynamicAxProfile;

/**
 * The mapper class to map AX definition of payment method to Connect payment method.
 *
 * <pre>
 * Ref Link : #1567:AX-Sales: Add additional payment types
 * </pre>
 *
 */
@Component
@DynamicAxProfile
public class AxDefaultPaymentMethodTranslator extends PaymentMethodTranslator {

  @Override
  public void afterPropertiesSet() throws Exception {
    this.getPaymentMethodMap().putIfAbsent(PaymentMethodType.CASH, AxPaymentMethod.CASH);
    this.getPaymentMethodMap().putIfAbsent(PaymentMethodType.CREDIT, AxPaymentMethod.RECHNUNG);
    this.getPaymentMethodMap().putIfAbsent(PaymentMethodType.DIRECT_INVOICE,
        AxPaymentMethod.DIRECTINVOICE);
    this.getPaymentMethodMap().putIfAbsent(PaymentMethodType.CARD, AxPaymentMethod.CARD);
  }

}
