package com.sagag.eshop.service.helper;

import com.sagag.eshop.repo.entity.invoice.InvoiceHistoryPosition;
import com.sagag.services.domain.sag.invoice.InvoicePositionDto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Component
public class InvoiceHistoryPositionConverter
    implements Function<InvoiceHistoryPosition, InvoicePositionDto> {

  @Override
  public InvoicePositionDto apply(InvoiceHistoryPosition invoiceHistoryPosition) {
    if (invoiceHistoryPosition == null) {
      return null;
    }
    final double amount =
        Objects.isNull(invoiceHistoryPosition.getPrice()) ? NumberUtils.DOUBLE_ZERO
            : (double) invoiceHistoryPosition.getPrice();
    final int quantity =
        Objects.isNull(invoiceHistoryPosition.getQuantity()) ? NumberUtils.INTEGER_ZERO
            : (int) invoiceHistoryPosition.getQuantity();

    return InvoicePositionDto.builder().invoiceNr(invoiceHistoryPosition.getInvoiceNr().toString())
        .amount(amount)
        .deliveryNoteNr(StringUtils.defaultString(invoiceHistoryPosition.getPackSlips()))
        .articleNr(StringUtils.defaultString(invoiceHistoryPosition.getArticleErpNr()))
        .articleTitle(StringUtils.defaultString(invoiceHistoryPosition.getArticleDescription()))
        .quantity(quantity).build();
  }
}
