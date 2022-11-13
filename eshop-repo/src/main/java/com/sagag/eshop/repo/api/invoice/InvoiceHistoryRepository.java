package com.sagag.eshop.repo.api.invoice;

import com.sagag.eshop.repo.entity.invoice.InvoiceHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

public interface InvoiceHistoryRepository extends JpaRepository<InvoiceHistory, String> {

  @Query(value = "select i from InvoiceHistory i "
      + "where i.customerNr = :customerNr "
      + "and i.createdDate >= :dateFrom "
      + "and i.createdDate <= :dateTo "
      + "and i.sourceFrom = :sourceFrom")
  List<InvoiceHistory> findByDateAndCustomer(@Param("customerNr") Integer customerNr,
      @Param("dateFrom") @Temporal(TemporalType.DATE) Date dateFrom,
      @Param("dateTo") @Temporal(TemporalType.DATE) Date dateTo,
      @Param("sourceFrom") String sourceFrom);

  @Query(value = "select i from InvoiceHistory i "
      + "where i.invoiceNr = :invoiceNr "
      + "and i.sourceFrom = :sourceFrom")
  List<InvoiceHistory> findByInvoiceNr(@Param("invoiceNr") Long invoiceNr,
      @Param("sourceFrom") String sourceFrom);
}
