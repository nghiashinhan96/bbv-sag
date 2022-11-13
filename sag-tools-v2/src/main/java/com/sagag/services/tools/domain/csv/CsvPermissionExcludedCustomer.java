package com.sagag.services.tools.domain.csv;

import java.io.Serializable;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class CsvPermissionExcludedCustomer implements Serializable {

  private static final long serialVersionUID = 1037174628779933909L;

  @CsvBindByName(column = "EXCLUDED_CUSTOMER_NR")
  private Long customerNr;
}
