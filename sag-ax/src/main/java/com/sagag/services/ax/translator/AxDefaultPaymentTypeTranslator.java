package com.sagag.services.ax.translator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.enums.AxPaymentType;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.DynamicAxProfile;

import lombok.extern.slf4j.Slf4j;

/**
 * The mapper class to map AX definition of payment type to Connect payment.
 *
 */
@Slf4j
@DynamicAxProfile
@Component
public class AxDefaultPaymentTypeTranslator extends AxPaymentTypeTranslator {

  @Override
  public PaymentMethodType translateToConnect(final String axPaymentType) {
    log.debug("Map ax payment type = {} to connect payment type for customer", axPaymentType);
    if (StringUtils.isBlank(axPaymentType)) {
      final String msg = "The given ax payment type must not be empty";
      throw new AxCustomerException(AxCustomerException.AxCustomerErrorCase.AC_PAYMENT_TYPE_001, msg);
    }

    switch (AxPaymentType.findByCode(axPaymentType)) {
      case FIN:
      case KEINE:
      case SELBSTZAHL:
      case SEPA_B_FIN:
      case SEPA_B2B:
      case SEPA_C_FIN:
      case SEPA_CORE:
        return PaymentMethodType.CREDIT;
      case BAR:
      case BETREIBUNG:
      case KARTE:
      case SAK:
      case SEPA_B_SAK:
      case SEPA_C_SAK:
      case SOFORT:
        return PaymentMethodType.CASH;
      default:
        final String msg = "No support this payment type = " + axPaymentType;
        throw new AxCustomerException(AxCustomerException.AxCustomerErrorCase.AC_PAYMENT_TYPE_002, msg);
    }
  }

}
