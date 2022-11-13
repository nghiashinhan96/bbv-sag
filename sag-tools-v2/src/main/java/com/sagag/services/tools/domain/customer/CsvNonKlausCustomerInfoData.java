package com.sagag.services.tools.domain.customer;

import com.opencsv.bean.CsvBindByPosition;
import com.sagag.services.tools.support.SupportedAffiliate;

import lombok.Data;

import java.io.Serializable;

@Data
public class CsvNonKlausCustomerInfoData implements Serializable {

  private static final long serialVersionUID = 2081970875870372427L;

  @CsvBindByPosition(position = 0)
  private String customerNr;

  public CsvCustomerInfoData build() {
    CsvCustomerInfoData item = new CsvCustomerInfoData();
    item.setCustomerNr(getCustomerNr());
    item.setAffiliate(SupportedAffiliate.DERENDINGER_CH);
    return item;
  }

}
