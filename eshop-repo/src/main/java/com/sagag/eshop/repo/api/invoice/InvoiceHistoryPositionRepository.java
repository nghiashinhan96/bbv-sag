package com.sagag.eshop.repo.api.invoice;

import com.sagag.eshop.repo.entity.invoice.InvoiceHistoryPosition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceHistoryPositionRepository
    extends JpaRepository<InvoiceHistoryPosition, String> {

  List<InvoiceHistoryPosition> findAllByInvoiceNr(Long invoiceNr);
}
