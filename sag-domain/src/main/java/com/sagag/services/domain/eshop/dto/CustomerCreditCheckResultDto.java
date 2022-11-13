package com.sagag.services.domain.eshop.dto;

import com.sagag.services.common.enums.PaymentMethodType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreditCheckResultDto implements Serializable {

  private static final long serialVersionUID = 7664718822923216371L;

  /**
   * Available customer credit
   */
  private double customerCredit;

  /**
   * Depend on payment method and affiliate.
   */
  private double additionalCredit;

  private boolean valid;

  /**
   * Used payment method, either from provided context or user setting.
   */
  private String paymentMethod;

  private List<CustomerCreditCheckResultDto> creditCheckResultByPaymentMethod;

  public static boolean isAlwaysValid(PaymentMethodType paymentMethod) {
    return PaymentMethodType.CASH == paymentMethod || PaymentMethodType.CARD == paymentMethod;
  }

  public static CustomerCreditCheckResultDto of(final PaymentMethodType paymentMethod,
      final double customerCredit) {
    final CustomerCreditCheckResultDto response = new CustomerCreditCheckResultDto();
    response.setValid(true);
    response.setPaymentMethod(paymentMethod.name());
    response.setCustomerCredit(customerCredit);
    return response;
  }

  public static CustomerCreditCheckResultDto process(final PaymentMethodType paymentMethod,
      final double customerCredit, final double grandTotal, final double additionalCredit) {
    final CustomerCreditCheckResultDto response = new CustomerCreditCheckResultDto();
    response.setValid(grandTotal <= customerCredit + additionalCredit);
    response.setPaymentMethod(paymentMethod.name());
    response.setCustomerCredit(customerCredit);
    response.setAdditionalCredit(additionalCredit);
    return response;
  }
}
