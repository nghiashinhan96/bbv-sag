package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.InvoiceType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InvoiceTypeRepository extends JpaRepository<InvoiceType, Integer> {

  Optional<InvoiceType> findOneById(Integer id);

  Optional<InvoiceType> findOneByInvoiceTypeCode(String invoiceTypeCode);

  @Query("select i.invoiceTypeCode from InvoiceType i where i.invoiceTypeName=:invoiceTypeName")
  Optional<String> findInvoiceTypeCodeByInvoiceTypeName(
      @Param("invoiceTypeName") String invoiceTypeName);
}
