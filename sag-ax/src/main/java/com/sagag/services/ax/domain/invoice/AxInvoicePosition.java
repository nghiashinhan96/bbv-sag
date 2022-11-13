package com.sagag.services.ax.domain.invoice;

import com.sagag.services.domain.sag.invoice.InvoicePositionDto;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AxInvoicePosition implements Serializable {

  private static final long serialVersionUID = 6257782038843572987L;

  private String invoiceNr;

  private String articleId;

  private Integer quantity;

  private String orderNr;

  private String deliveryNoteNr;

  private Double amount;

  private Integer sequence;

  public InvoicePositionDto toPositionDto() {
    return InvoicePositionDto.builder().invoiceNr(invoiceNr)
        .articleId(articleId)
        .quantity(quantity)
        .orderNr(orderNr)
        .deliveryNoteNr(deliveryNoteNr)
        .amount(amount)
        .sequence(sequence).build();
  }
}

