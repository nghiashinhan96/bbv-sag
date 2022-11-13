package com.sagag.services.ax.translator.wint;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.enums.wint.WtPaymentType;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.ax.translator.AxPaymentTypeTranslator;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.WintProfile;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@WintProfile
public class WintPaymentTypeTranslator extends AxPaymentTypeTranslator {

  @Override
  public PaymentMethodType translateToConnect(final String wtPaymentType) {
    log.debug("Map Wint payment type = {} to connect payment type for customer", wtPaymentType);
    if (StringUtils.isBlank(wtPaymentType)) {
      log.warn("The given Wint payment type is empty - default to {}",
          PaymentMethodType.CASH.toString());
      return PaymentMethodType.CASH;
    }

    switch (WtPaymentType.findByCode(wtPaymentType)) {
      case CASH:
        return PaymentMethodType.CASH;
      case WHOLESALE:
        return PaymentMethodType.WHOLESALE;
      default:
        final String msg = "No support this payment type = " + wtPaymentType;
        throw new AxCustomerException(AxCustomerException.AxCustomerErrorCase.AC_PAYMENT_TYPE_002,
            msg);
    }
  }

}
