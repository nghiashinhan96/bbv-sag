package com.sagag.services.ax.translator;

import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.sagag.services.ax.enums.AxPaymentMethod;
import com.sagag.services.common.enums.PaymentMethodType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PaymentMethodTranslator
  implements AxDataTranslator<String, String>, InitializingBean {

  @Getter
  private Map<PaymentMethodType, AxPaymentMethod> paymentMethodMap = new EnumMap<>(
      PaymentMethodType.class);

  @Override
  public String translateToConnect(String axPaymentMethod) {
    throw new UnsupportedOperationException("No support translate to connect payment type");
  }

  @Override
  public String translateToAx(String eConnectPaymentMethod) {
    log.debug("Map payment method = {} to ax payment method", eConnectPaymentMethod);
    if (StringUtils.isBlank(eConnectPaymentMethod) || this.paymentMethodMap == null) {
      return StringUtils.EMPTY;
    }
    final AxPaymentMethod axPaymentMethod =
        this.getPaymentMethodMap().get(PaymentMethodType.valueOf(eConnectPaymentMethod));
    return axPaymentMethod != null ? axPaymentMethod.name() : StringUtils.EMPTY;
  }

}
