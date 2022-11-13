package com.sagag.services.tools.ax.translator;

import com.sagag.services.tools.exception.AxCustomerException;
import com.sagag.services.tools.support.SupportedAffiliate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The mapper class to map AX sales order pool with company name .
 * <p>
 * Not support for Klaus and Remco.
 *
 */
@Component
public class AxSalesOrderPoolTranslator implements AxDataTranslator<String, SupportedAffiliate> {

  private static final String DDAT_SALES_ORDER_POOL = "01";

  private static final String MATIK_AT_SALES_ORDER_POOL = "02";

  private static final String DDCH_SALES_ORDER_POOL = "03";

  private static final String TECHNOMAG_SALES_ORDER_POOL = "04";

  private static final String MATIK_CH_SALES_ORDER_POOL = "05";

  private static final String WBB_SALES_ORDER_POOL = "06";

  private static final String KLAUS_SALES_ORDER_POOL = "07";

  private static final Map<String, SupportedAffiliate> SUPPORTED_AFFILIATES = new HashMap<>();

  static {
    SUPPORTED_AFFILIATES.put(DDAT_SALES_ORDER_POOL, SupportedAffiliate.DERENDINGER_AT);
    SUPPORTED_AFFILIATES.put(MATIK_AT_SALES_ORDER_POOL, SupportedAffiliate.MATIK_AT);
    SUPPORTED_AFFILIATES.put(DDCH_SALES_ORDER_POOL, SupportedAffiliate.DERENDINGER_CH);
    SUPPORTED_AFFILIATES.put(TECHNOMAG_SALES_ORDER_POOL, SupportedAffiliate.TECHNOMAG);
    SUPPORTED_AFFILIATES.put(MATIK_CH_SALES_ORDER_POOL, SupportedAffiliate.MATIK_CH);
    SUPPORTED_AFFILIATES.put(WBB_SALES_ORDER_POOL, SupportedAffiliate.WBB);
    SUPPORTED_AFFILIATES.put(KLAUS_SALES_ORDER_POOL, SupportedAffiliate.KLAUS);
  }

  @Override
  public SupportedAffiliate translateToConnect(String salesOrderPool) {
    if (StringUtils.isBlank(salesOrderPool)) {
      final String msg = "The given sales order pool must not be empty";
      throw new AxCustomerException(AxCustomerException.AxCustomerErrorCase.AC_SALES_ORDER_POOL_001, msg);
    }
    final SupportedAffiliate affilate = SUPPORTED_AFFILIATES.get(salesOrderPool);
    if (Objects.isNull(affilate)) {
      final String msg = "Not support this affiliate yet";
      throw new AxCustomerException(AxCustomerException.AxCustomerErrorCase.AC_SALES_ORDER_POOL_002, msg);
    }
    return affilate;
  }

  @Override
  public String translateToAx(SupportedAffiliate affiliate) {
    throw new UnsupportedOperationException("Not implementation yet");
  }

}
