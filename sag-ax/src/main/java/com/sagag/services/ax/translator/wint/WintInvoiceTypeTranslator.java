package com.sagag.services.ax.translator.wint;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.enums.wint.WtInvoiceType;
import com.sagag.services.ax.translator.AxInvoiceTypeTranslator;
import com.sagag.services.common.enums.ErpInvoiceTypeCode;
import com.sagag.services.common.profiles.WintProfile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WintProfile
@Component
public class WintInvoiceTypeTranslator extends AxInvoiceTypeTranslator {

  @Override
  public ErpInvoiceTypeCode translateToConnect(String wtInvoiceType) {
    log.debug("Map Wint invoice type = {} to connect invoice type", wtInvoiceType);
    final ErpInvoiceTypeCode defaultErpInvoiceTypeCode = ErpInvoiceTypeCode.SINGLE_INVOICE;
    if (StringUtils.isBlank(wtInvoiceType)) {
      log.warn("The given Wint invoice type is empty - default to {}", defaultErpInvoiceTypeCode);
      return defaultErpInvoiceTypeCode;
    }

    switch (WtInvoiceType.findByCode(wtInvoiceType)) {
      case WEEKLY:
        return ErpInvoiceTypeCode.WEEKLY_INVOICE;
      case SINGLE:
      default:
        return defaultErpInvoiceTypeCode;
    }
  }

}