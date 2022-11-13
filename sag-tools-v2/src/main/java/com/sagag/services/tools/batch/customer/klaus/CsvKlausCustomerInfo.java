package com.sagag.services.tools.batch.customer.klaus;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

import java.io.Serializable;

@Data
public class CsvKlausCustomerInfo implements Serializable {

  private static final long serialVersionUID = 2029191706602233953L;

  @CsvBindByName(column = "CUSTOMER_NUMBER", locale = "US")
  private String customerNr;

  public CsvKlausCustomerInfo(String custNr) {
    setCustomerNr(custNr);
  }
}
