package com.sagag.services.ax.converter;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.domain.invoice.AxInvoice;
import com.sagag.services.ax.translator.AxPaymentTypeTranslator;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.invoice.InvoiceDto;

@Component
@AxProfile
public class AxInvoiceConverter implements Function<AxInvoice, InvoiceDto> {

  @Autowired
  private AxPaymentTypeTranslator axPaymentTypeTranslator;

  @Override
  public InvoiceDto apply(AxInvoice axInvoice) {
    if (axInvoice == null) {
      return null;
    }
    final InvoiceDto invoice = axInvoice.toDto();
    invoice.setAmount(axInvoice.getAmount());
    final PaymentMethodType paymentMethodType = axPaymentTypeTranslator.translateToConnect(
      axInvoice.getPaymentType());
    invoice.setPaymentType(paymentMethodType.name());
    return invoice;
  }
}
