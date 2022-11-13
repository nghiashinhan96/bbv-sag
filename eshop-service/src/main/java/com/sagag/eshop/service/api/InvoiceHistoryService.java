package com.sagag.eshop.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.sag.invoice.InvoiceDto;

import java.util.List;
import java.util.Optional;

public interface InvoiceHistoryService {

  List<InvoiceDto> search(String customerNr, String dateFrom, String dateTo);

  Optional<InvoiceDto> getInvoiceDetail(UserInfo user, String invoiceNr, String orderNr);

}
