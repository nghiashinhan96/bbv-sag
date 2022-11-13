package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.InvoiceTypeRepository;
import com.sagag.eshop.repo.entity.InvoiceType;
import com.sagag.eshop.service.api.InvoiceTypeService;
import com.sagag.services.common.enums.ErpInvoiceTypeCode;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
public class InvoiceTypeServiceImpl implements InvoiceTypeService {

  @Autowired
  InvoiceTypeRepository invoiceTypeRepo;

  @Override
  public Optional<InvoiceType> getInvoiceTypeById(Integer id) {
    return invoiceTypeRepo.findOneById(id);
  }

  @Override
  public Optional<InvoiceType> getInvoiceTypeByCode(final String invoiceTypeCode) {
    ErpInvoiceTypeCode erpInvoiceTypeCode;
    if (!ErpInvoiceTypeCode.contains(invoiceTypeCode)) {
      log.debug("The invoice type code " + invoiceTypeCode + " was not defined!" + " Set it as "
          + ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE.name());
      erpInvoiceTypeCode = ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE;
    } else {
      erpInvoiceTypeCode = ErpInvoiceTypeCode.valueOf(invoiceTypeCode);
    }
    return invoiceTypeRepo.findOneByInvoiceTypeCode(erpInvoiceTypeCode.name());
  }
}
