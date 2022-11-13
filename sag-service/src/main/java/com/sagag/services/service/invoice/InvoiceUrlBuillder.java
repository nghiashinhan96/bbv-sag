package com.sagag.services.service.invoice;

import com.sagag.eshop.repo.api.invoice.InvoiceHistoryRepository;
import com.sagag.eshop.repo.entity.invoice.InvoiceHistory;
import com.sagag.eshop.repo.enums.InvoiceHistorySource;
import com.sagag.services.article.api.InvoiceExternalService;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Component
public class InvoiceUrlBuillder {

  @Value("${invoice.history.url:}")
  private String sagsysInvoiceUrl;

  @Autowired
  private InvoiceExternalService axInvoiceService;

  @Autowired
  private InvoiceHistoryRepository invoiceHistoryRepo;

  private static final String PDF_EXTENSION = ".PDF";

  public String build(SupportedAffiliate affiliate, String custNr, String invoiceNr,
      boolean oldInvoice, String orderNr) {
    if (oldInvoice) {
      List<InvoiceHistory> invoices = invoiceHistoryRepo.findByInvoiceNr(Long.valueOf(invoiceNr),
          InvoiceHistorySource.SAGSYS.toString());
      InvoiceHistory defaultOrder =
          invoices.stream().findFirst().orElseThrow(notFoundPdfUrl(invoiceNr));
      InvoiceHistory selectedOrder = invoices.stream()
          .filter(i -> Objects.nonNull(i.getOrderNr()) && i.getOrderNr().toString().equals(orderNr))
          .findFirst().orElse(defaultOrder);
      String docId = selectedOrder.getDocId();
      String companyDb = InvoiceCompanyDb.fromDesc(affiliate.getAffiliate()).getCompanyDb();
      String year = docId.substring(0, 4);
      String month = docId.substring(4, 6);

      return new StringBuilder(sagsysInvoiceUrl)
          .append(companyDb).append(SagConstants.SLASH)
          .append(year).append(SagConstants.SLASH)
          .append(month).append(SagConstants.SLASH)
          .append(docId).append(PDF_EXTENSION).toString();
    }

    return axInvoiceService.getInvoicePdfUrl(affiliate.getCompanyName(), custNr,
        invoiceNr).orElseThrow(notFoundPdfUrl(invoiceNr));
  }

  private static Supplier<IllegalArgumentException> notFoundPdfUrl(String invoiceNr) {
    return () -> {
      final String msg = String.format("Not found PDF URL of invoice number = %s", invoiceNr);
      return new IllegalArgumentException(msg);
    };
  }
}
