package com.sagag.eshop.service.helper;

import com.sagag.eshop.repo.entity.invoice.InvoiceHistory;
import com.sagag.services.domain.sag.invoice.InvoicePositionDto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Component
public class InvoiceHistoryToInvoicePositionConverter
    implements Function<InvoiceHistory, InvoicePositionDto> {

  @Override
  public InvoicePositionDto apply(InvoiceHistory sagsysInvoice) {
    if (sagsysInvoice == null) {
      return null;
    }
    final String deliveryNoteNr = Objects.isNull(sagsysInvoice.getDeliveryNr()) ? StringUtils.EMPTY
        : sagsysInvoice.getDeliveryNr().toString();
    final String orderNr = Objects.isNull(sagsysInvoice.getOrderNr()) ? StringUtils.EMPTY
        : sagsysInvoice.getOrderNr().toString();
    final double amount = Objects.isNull(sagsysInvoice.getPrice()) ? NumberUtils.DOUBLE_ZERO
        : (double) sagsysInvoice.getPrice();
    return InvoicePositionDto.builder().invoiceNr(sagsysInvoice.getInvoiceNr().toString())
        .deliveryNoteNr(deliveryNoteNr).orderNr(orderNr)
        .amount(amount)
        .build();
  }
}
