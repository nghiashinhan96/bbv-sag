package com.sagag.eshop.service.helper;

import com.sagag.eshop.repo.entity.invoice.InvoiceHistory;
import com.sagag.services.domain.sag.invoice.InvoiceDto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Component
public class InvoiceHistoryConverter implements Function<InvoiceHistory, InvoiceDto> {

  @Override
  public InvoiceDto apply(InvoiceHistory sagsysInvoice) {
    if (sagsysInvoice == null) {
      return null;
    }

    return InvoiceDto.builder().invoiceNr(sagsysInvoice.getInvoiceNr().toString())
        .invoiceDate(sagsysInvoice.getCreatedDate()).name(sagsysInvoice.getCompanyName())
        .customerNr(sagsysInvoice.getCustomerNr().toString())
        .zipcode(Objects.isNull(sagsysInvoice.getPostCode()) ? StringUtils.EMPTY
            : sagsysInvoice.getPostCode().toString())
        .country(StringUtils.defaultString(sagsysInvoice.getCountry()))
        .city(StringUtils.defaultString(sagsysInvoice.getPlace())).termOfPayment(StringUtils.EMPTY)
        .price(Objects.isNull(sagsysInvoice.getPrice()) ? NumberUtils.DOUBLE_ZERO
            : (double) sagsysInvoice.getPrice())
        .paymentType(SagSysPaymentMethod.fromDesc(sagsysInvoice.getCashCredit()).toString())
        .deliveryNoteNr(Objects.isNull(sagsysInvoice.getDeliveryNr()) ? StringUtils.EMPTY
            : sagsysInvoice.getDeliveryNr().toString())
        .orderNr(Objects.isNull(sagsysInvoice.getOrderNr()) ? StringUtils.EMPTY
            : sagsysInvoice.getOrderNr().toString())
        .build();
  }

}
