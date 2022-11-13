package com.sagag.services.ax.translator;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.eshop.repo.api.InvoiceTypeRepository;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.common.enums.ErpInvoiceTypeCode;
import com.sagag.services.common.profiles.DynamicAxProfile;

import lombok.extern.slf4j.Slf4j;

/**
 * The mapper class to map AX definition of send method to Connect payment.
 *
 * <pre>
 * Ref Link : #1535:
 * </pre>
 *
 */
@Slf4j
@DynamicAxProfile
@Component
public class AxDefaultInvoiceTypeTranslator extends AxInvoiceTypeTranslator {

  @Autowired
  private InvoiceTypeRepository invoiceTypeRepo;

  @Override
  public ErpInvoiceTypeCode translateToConnect(String axInvoiceType) {
    log.debug("Map ax invoice type = {} to connect invoice type", axInvoiceType);
    if (StringUtils.isBlank(axInvoiceType)) {
      String msg = "The given ax invoice type must not be empty";
      throw new AxCustomerException(
          AxCustomerException.AxCustomerErrorCase.AC_INVOICE_TYPE_001, msg);
    }

    final Optional<String> invoiceTypeCode =
        invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(axInvoiceType);
    if (!invoiceTypeCode.isPresent() || !ErpInvoiceTypeCode.contains(invoiceTypeCode.get())) {
      log.warn("Returns to ALL as default invoice for ax invoice is not support at Connect is {}",
          axInvoiceType);
      return ErpInvoiceTypeCode.ALL;
    }
    return ErpInvoiceTypeCode.valueOf(invoiceTypeCode.get());
  }

}