package com.sagag.services.article.api;

import com.sagag.services.article.api.request.InvoiceExternalSearchRequest;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.invoice.InvoiceDto;

import java.util.List;
import java.util.Optional;

public interface InvoiceExternalService {

  /**
   * Returns the found invoices by search request.
   *
   * @param compName the affiliate company name
   * @param custNr the customer number
   * @param request the invoice search request
   * @return the list of of {@link InvoiceDto}
   */
  List<InvoiceDto> searchInvoices(String compName, String custNr, InvoiceExternalSearchRequest request);

  /**
   * Returns the invoice detail by invoice number.
   *
   * @param compName the affiliate company name
   * @param custNr the customer number
   * @param invoiceNr the invoice number
   * @param orderNr the order number
   * @param invoiceAddress the user invoice address
   * @return the optional of {@link InvoiceDto}
   */
  Optional<InvoiceDto> getInvoiceDetail(String compName, String custNr, String invoiceNr,
    String orderNr, Optional<Address> invoiceAddress);

  /**
   * Returns the PDF URL of invoices
   *
   * @param compName the affiliate company name
   * @param custNr the customer number
   * @param invoiceNr the invoice number
   * @return the optional of PDF url
   */
  Optional<String> getInvoicePdfUrl(String compName, String custNr, String invoiceNr);

}
