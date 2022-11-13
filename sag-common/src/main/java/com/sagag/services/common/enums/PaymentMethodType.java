package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum PaymentMethodType {

  CREDIT("payment.credit"),
  CASH("payment.cash"),
  CARD("payment.card"),
  DIRECT_INVOICE("payment.direct_invoice"),
  BANK_TRANSFER("payment.bank_transfer"),
  EUR_PAYMENT("payment.eur_payment"),
  WHOLESALE("payment.wholesale"); // For SB

  private String msgCode;

  public boolean isCredit() {
    return CREDIT == this;
  }

  public boolean isCash() {
    return CASH == this;
  }

  public boolean isCard() {
    return CARD == this;
  }

  public boolean isDirectInvoice() {
    return DIRECT_INVOICE == this;
  }

  public boolean justAllowForSales() {
    return isCard() || isDirectInvoice();
  }

  /**
   * The list of allowed payment method for Sales on behalf.
   *
   */
  public static List<String> getPaymentMethodsForSales() {
    return Stream.of(values()).map(PaymentMethodType::name).collect(Collectors.toList());
  }

  /**
   * The list of allowed payment method for B2B users.
   *
   */
  public static List<String> getPaymentMethodsForB2BUsers() {
    return Stream.of(CASH, CREDIT).map(PaymentMethodType::name).collect(Collectors.toList());
  }

  public static PaymentMethodType valueOfIgnoreCase(String paymentMethodIgnorseCase) {
    return Stream.of(values())
        .filter(value -> StringUtils.equalsIgnoreCase(value.name(), paymentMethodIgnorseCase))
        .findFirst().orElseThrow(() -> new NoSuchElementException(
            "Not found legal PaymentMethodType for value string " + paymentMethodIgnorseCase));
  }

}
