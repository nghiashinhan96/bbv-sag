package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.io.Serializable;

@Data
public class CsvAXCustomer implements Serializable {

  private static final long serialVersionUID = -4179025985092219135L;

  @CsvBindByName(column = "CUSTOMERACCOUNT")
  private String customerNr;
}
