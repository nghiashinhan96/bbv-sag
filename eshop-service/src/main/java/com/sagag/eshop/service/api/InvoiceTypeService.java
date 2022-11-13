package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.InvoiceType;

import java.util.Optional;

public interface InvoiceTypeService {

  /**
   * Returns {@link InvoiceType} by id.
   *
   * @param id the id to search invoiceType.
   * @return the null-able invoiceType.
   */
  Optional<InvoiceType> getInvoiceTypeById(Integer id);

  /**
   * Returns {@link InvoiceType} by invoiceTypeCode.
   *
   * @param invoiceTypeCode the invoiceTypeCode
   * @return the null-able invoiceType.
   */
  Optional<InvoiceType> getInvoiceTypeByCode(String invoiceTypeCode);

}
