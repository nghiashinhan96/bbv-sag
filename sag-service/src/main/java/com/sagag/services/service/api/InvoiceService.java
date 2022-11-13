package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.service.request.invoice.InvoiceSearchRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {

  /**
   * Returns invoices of user login.
   *
   * @param user the user login
   * @param request the invoice search request
   * @return the list of {@link InvoiceDto}
   */
  List<InvoiceDto> searchInvoices(UserInfo user, InvoiceSearchRequest request);

  /**
   * Returns invoice detail of user login.
   *
   * @param user the user login
   * @param invoiceNr the invoice number
   * @param simpleMode the flag to provide more info or not
   * @param oldInvoice the flag to get sagsys invoice
   * @return the optional of {@link InvoiceDto}
   */
  Optional<InvoiceDto> getInvoiceDetail(UserInfo user, String invoiceNr, String orderNr,
      boolean simpleMode, boolean oldInvoice);

  /**
   * Streams invoice PDF to client side.
   *
   * @param affiliate the affiliate of user login
   * @param custNr the customer of user login
   * @param invoiceNr the invoice number to stream file
   * @param orderNr the order number
   * @param oldInvoice the flag to get sagsys invoice
   * @return the object of {@link ExportStreamedResult}
   * @throws java.io.IOException throw if got any problems related stream file
   */
  ExportStreamedResult streamInvoicePdf(SupportedAffiliate affiliate, String custNr,
      String invoiceNr, boolean oldInvoice, String orderNr) throws IOException;
}
