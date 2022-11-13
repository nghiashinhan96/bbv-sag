package com.sagag.services.tools.ax.translator;

import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.tools.domain.ax.AxPaymentMethod;
import com.sagag.services.tools.support.PaymentMethodType;

import lombok.extern.slf4j.Slf4j;

/**
 * The mapper class to map AX definition of payment method to Connect payment method.
 *
 * <pre>
 * Ref Link : #1567:AX-Sales: Add additional payment types
 * </pre>
 *
 */
@Component
@Slf4j
public class AxPaymentMethodTranslator implements AxDataTranslator<String, String> {

  private static final Map<PaymentMethodType, AxPaymentMethod> CONNECT_MAPPING_PAYMENT_METHODS =
      new EnumMap<>(PaymentMethodType.class);

  static {
    CONNECT_MAPPING_PAYMENT_METHODS.putIfAbsent(PaymentMethodType.CASH, AxPaymentMethod.CASH);
    CONNECT_MAPPING_PAYMENT_METHODS.putIfAbsent(PaymentMethodType.CREDIT, AxPaymentMethod.RECHNUNG);
    CONNECT_MAPPING_PAYMENT_METHODS.putIfAbsent(PaymentMethodType.DIRECT_INVOICE, AxPaymentMethod.DIRECTINVOICE);
    CONNECT_MAPPING_PAYMENT_METHODS.putIfAbsent(PaymentMethodType.CARD, AxPaymentMethod.CARD);
  }

  @Override
  public String translateToConnect(String axPaymentMethod) {
    throw new UnsupportedOperationException("No support translate to connect payment type");
  }

  @Override
  public String translateToAx(String eConnectPaymentMethod) {
    log.debug("Map payment method = {} to ax payment method", eConnectPaymentMethod);
    if (StringUtils.isBlank(eConnectPaymentMethod)) {
      return StringUtils.EMPTY;
    }
    return CONNECT_MAPPING_PAYMENT_METHODS.get(
        PaymentMethodType.valueOf(eConnectPaymentMethod)).name();
  }

}
