package com.sagag.services.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.sag.invoice.InvoiceDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.collections4.ListUtils;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class InvoiceResource extends ResourceSupport {

  @JsonProperty("invoiceDetailDtos")
  private final List<InvoiceDto> invoices;

  public int getTotalItems() {
    return ListUtils.emptyIfNull(invoices).size();
  }

}
