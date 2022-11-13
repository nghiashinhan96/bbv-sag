package com.sagag.services.ax.translator;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.common.enums.SupportedAffiliate;

/**
 * The mapper class to map AX sales order pool with company name .
 * <p>
 * Not support for Klaus and Remco.
 *
 */

public abstract class AxSalesOrderPoolTranslator implements AxDataTranslator<String, SupportedAffiliate> {

  private static final Map<String, SupportedAffiliate> SUPPORTED_AFFILIATES = new HashMap<>();

  static {
    for (SupportedAffiliate affiliate : SupportedAffiliate.values()) {
      if (!StringUtils.isBlank(affiliate.getSalesOrderPool())) {
        SUPPORTED_AFFILIATES.put(affiliate.getSalesOrderPool(), affiliate);
      }
    }
  }

  @Override
  public SupportedAffiliate translateToConnect(String salesOrderPool) {
    if (StringUtils.isBlank(salesOrderPool)) {
      final String msg = "The given sales order pool must not be empty";
      throw new AxCustomerException(
          AxCustomerException.AxCustomerErrorCase.AC_SALES_ORDER_POOL_001, msg);
    }
    final SupportedAffiliate affiliate = SUPPORTED_AFFILIATES.get(salesOrderPool);
    if (Objects.isNull(affiliate)) {
      final String msg = "Not support this affiliate yet";
      throw new AxCustomerException(
          AxCustomerException.AxCustomerErrorCase.AC_SALES_ORDER_POOL_002, msg);
    }
    return affiliate;
  }

  @Override
  public String translateToAx(SupportedAffiliate affiliate) {
    throw new UnsupportedOperationException("Not implementation yet");
  }

}
