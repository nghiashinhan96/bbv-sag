package com.sagag.services.tools.ax.translator;

import com.sagag.services.tools.exception.AxCustomerException;
import com.sagag.services.tools.repository.target.InvoiceTypeRepository;
import com.sagag.services.tools.support.AxInvoiceTypes;
import com.sagag.services.tools.support.ErpInvoiceTypeCode;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The mapper class to map AX definition of send method to Connect payment.
 *
 * <pre>
 * Ref Link : #1535:
 * </pre>
 *
 */
@Component
@Slf4j
public class AxInvoiceTypeTranslator implements AxDataTranslator<String, ErpInvoiceTypeCode> {

  @Autowired
  private InvoiceTypeRepository invoiceTypeRepo;

  private static final Map<String, ErpInvoiceTypeCode> CONNECT_INVOICE_TYPE_CODES = new HashMap<>();

  static {
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_2WFAKT, ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_2WFAKTGT, ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE_WITH_CREDIT_SEPARATION);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_EINZELFAGT, ErpInvoiceTypeCode.SINGLE_INVOICE_WITH_CREDIT_SEPARATION);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_EINZELFAKT, ErpInvoiceTypeCode.SINGLE_INVOICE);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_MONATSFAGT, ErpInvoiceTypeCode.MONTHLY_INVOICE_WITH_CREDIT_SEPARATION);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_MONATSFAKT, ErpInvoiceTypeCode.MONTHLY_INVOICE);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_TAGESFAGT, ErpInvoiceTypeCode.DAILY_INVOICE_WITH_CREDIT_SEPARATION);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_TAGESFAKT, ErpInvoiceTypeCode.DAILY_INVOICE);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_WOCHENFAGT, ErpInvoiceTypeCode.WEEKLY_INVOICE_WITH_CREDIT_SEPARATION);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_WOCHENFAKT, ErpInvoiceTypeCode.WEEKLY_INVOICE);

    // Add more invoice types mapping with ticket #2207
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_2WFAKTGX, ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE_WITH_CREDIT_SEPARATION);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_MONATSFAGX, ErpInvoiceTypeCode.MONTHLY_INVOICE_WITH_CREDIT_SEPARATION);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_MONATSFAGW, ErpInvoiceTypeCode.MONTHLY_INVOICE_WEEKLY_CREDIT_SEPARATION);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(
        AxInvoiceTypes.AX_WOCHENFAGX, ErpInvoiceTypeCode.WEEKLY_INVOICE_WITH_CREDIT_SEPARATION);

    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(AxInvoiceTypes.AX_WOCHENFAGE.toUpperCase(),
        ErpInvoiceTypeCode.WEEKLY_INVOICE_SINGLE_CREDIT);

    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(AxInvoiceTypes.AX_DEFAULT.toUpperCase(),
        ErpInvoiceTypeCode.DEFAULT);
    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(AxInvoiceTypes.AX_ALL.toUpperCase(),
        ErpInvoiceTypeCode.ALL);

    CONNECT_INVOICE_TYPE_CODES.putIfAbsent(AxInvoiceTypes.AX_ALLESAM.toUpperCase(),
        ErpInvoiceTypeCode.ACCUMULATIVE_INVOICE);
  }

  @Override
  public ErpInvoiceTypeCode translateToConnect(String axInvoiceType) {
    log.debug("Map ax invoice type = {} to connect invoice type", axInvoiceType);
    if (StringUtils.isBlank(axInvoiceType)) {
      String msg = "The given ax invoice type must not be empty";
      throw new AxCustomerException(AxCustomerException.AxCustomerErrorCase.AC_INVOICE_TYPE_001, msg);
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

  @Override
  public String translateToAx(ErpInvoiceTypeCode source) {
    throw new UnsupportedOperationException("No support this operation");
  }

}
