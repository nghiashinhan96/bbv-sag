package com.sagag.services.ax.translator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.enums.AxPaymentType;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.ax.paymenttypes.AxPaymentTypeMapper;
import com.sagag.services.common.enums.PaymentMethodType;

import lombok.extern.slf4j.Slf4j;

/**
 * The mapper class to map AX definition of payment type to Connect payment for sales.
 *
 */
@Component
@Slf4j
public class AxPaymentTypeForSalesTranslator implements AxDataTranslator<String, PaymentMethodType> {

  @Autowired
  private AxPaymentTypeMapper paymentTypeMapper;

  @Override
  public PaymentMethodType translateToConnect(final String axPaymentType) {
    log.debug("Map ax payment type = {} to connect payment type for sales", axPaymentType);
    if (StringUtils.isBlank(axPaymentType)) {
      final String msg = "The given ax payment type must not be empty";
      throw new AxCustomerException(AxCustomerException.AxCustomerErrorCase.AC_PAYMENT_TYPE_001, msg);
    }

    final PaymentMethodType paymentMethodType = paymentTypeMapper.getAxPaymentTypeMapperForSales()
        .get(AxPaymentType.findByCode(axPaymentType));
    if (paymentMethodType == null) {
      final String msg = "No support this payment type = " + axPaymentType;
      throw new AxCustomerException(AxCustomerException.AxCustomerErrorCase.AC_PAYMENT_TYPE_002, msg);
    }
    return paymentMethodType;
  }

  @Override
  public String translateToAx(PaymentMethodType axPaymentType) {
    throw new UnsupportedOperationException("No support this operation");
  }

}
