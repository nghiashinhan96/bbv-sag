package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Time;

@Data
@EqualsAndHashCode(callSuper = false)
public class CsvBranchOpeningHour implements Serializable {

  private static final long serialVersionUID = 7129360509774270438L;

  @CsvBindByName(column = "BRANCH_NUMBER")
  private Integer branchNr;

  @CsvBindByName(column = "BRANCH_CODE")
  private String branchCode;

  @CsvBindByName(column = "OPENING_TIME", locale = "US")
  @CsvDate("HH:mm:ss")
  private Time openingHour;

  @CsvBindByName(column = "CLOSING_TIME", locale = "US")
  @CsvDate("HH:mm:ss")
  private Time closingTime;
}
