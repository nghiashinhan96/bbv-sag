package com.sagag.services.ax.translator;

import com.sagag.services.common.enums.ErpInvoiceTypeCode;

/**
 * The mapper class to map AX definition of send method to Connect payment.
 *
 * <pre>
 * Ref Link : #1535:
 * </pre>
 *
 */
public abstract class AxInvoiceTypeTranslator
  implements AxDataTranslator<String, ErpInvoiceTypeCode> {

  @Override
  public String translateToAx(ErpInvoiceTypeCode source) {
    throw new UnsupportedOperationException("No support this operation");
  }

}