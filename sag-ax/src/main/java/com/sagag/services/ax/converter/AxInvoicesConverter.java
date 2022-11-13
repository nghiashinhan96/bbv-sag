package com.sagag.services.ax.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.domain.sag.invoice.InvoicePositionDto;

@Component
@AxProfile
public class AxInvoicesConverter implements UnaryOperator<List<InvoiceDto>> {

  @Override
  public List<InvoiceDto> apply(final List<InvoiceDto> invoices) {
    if (CollectionUtils.isEmpty(invoices)) {
      return Collections.emptyList();
    }

    // Group by delivery note
    final Map<String, List<InvoiceDto>> invoicesByDeliveryNoteNrs = new HashMap<>();
    invoices.stream().flatMap(invoice -> invoice.getDeliveryNoteNrs().stream())
        .forEach(deliveryNote -> groupInvocesByDeliveryNote(invoicesByDeliveryNoteNrs)
            .accept(deliveryNote, invoices));

    for (Map.Entry<String, List<InvoiceDto>> entry : invoicesByDeliveryNoteNrs.entrySet()) {
      final String deliveryNote = entry.getKey();
      findInvoiceByDlvrNt().apply(deliveryNote, entry.getValue())
        .ifPresent(invoice -> {
          Optional<InvoicePositionDto> positionOpt = invoice.getPositions().stream()
            .filter(position -> StringUtils.equals(position.getDeliveryNoteNr(), deliveryNote))
            .findFirst();

          positionOpt.ifPresent(position -> {
            invoice.setDeliveryNoteNr(position.getDeliveryNoteNr());
            invoice.setOrderNr(position.getOrderNr());
          });
        });
    }
    return invoicesByDeliveryNoteNrs.values().stream()
      .flatMap(List::stream)
      .filter(invoice -> !StringUtils.isBlank(invoice.getDeliveryNoteNr()))
      .sorted(Comparator.comparing(InvoiceDto::getInvoiceDate).reversed())
      .collect(Collectors.toList());
  }

  private static BiConsumer<String, List<InvoiceDto>> groupInvocesByDeliveryNote(
    final Map<String, List<InvoiceDto>> invoicesByDeliveryNoteNrs) {
    return (deliveryNote, invoices) -> findInvoiceByDlvrNt().apply(deliveryNote, invoices)
      .ifPresent(invoice -> invoicesByDeliveryNoteNrs.compute(deliveryNote,
        invoiceCompute(invoice)));
  }

  private static BiFunction<String, List<InvoiceDto>, Optional<InvoiceDto>> findInvoiceByDlvrNt() {
    return (deliveryNote, invoices) -> invoices.stream()
      .filter(invoicePositionPredicate(deliveryNote)).findFirst();
  }

  private static Predicate<InvoiceDto> invoicePositionPredicate(final String deliveryNote) {
    return invoice -> invoice.getPositions().stream()
      .anyMatch(pos -> StringUtils.equals(pos.getDeliveryNoteNr(), deliveryNote));
  }

  private static BiFunction<String, List<InvoiceDto>, List<InvoiceDto>> invoiceCompute(
    final InvoiceDto invoice) {
    return (deliveryNote, invoices) -> {
      if (invoices == null) {
        invoices = new ArrayList<>();
      }
      // Create new instance to avoid update reference classes.
      InvoiceDto invoiceDto = new InvoiceDto();
      SagBeanUtils.copyProperties(invoice, invoiceDto);
      invoices.add(invoiceDto);
      return invoices;
    };
  }
}
