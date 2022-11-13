package com.sagag.services.ax.translator.wint;

import org.springframework.stereotype.Component;

import com.sagag.services.ax.translator.AxSalesOrderPoolTranslator;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.WintProfile;

@Component
@WintProfile
public class WintSalesOrderPoolTranslator extends AxSalesOrderPoolTranslator {

  @Override
  public SupportedAffiliate translateToConnect(String salesOrderPool) {
    return SupportedAffiliate.WINT_SB;
  }

}
