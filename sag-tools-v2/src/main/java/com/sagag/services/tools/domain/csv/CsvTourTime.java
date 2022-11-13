package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

import java.io.Serializable;

@Data
public class CsvTourTime implements Serializable {

  private static final long serialVersionUID = -8843788252499233215L;

  @CsvBindByPosition(position = 0)
  private String customer;

  @CsvBindByPosition(position = 1)
  private String branchId;

  @CsvBindByPosition(position = 2)
  private String tourName;

  @CsvBindByPosition(position = 3)
  private String tourScheduledName;

  @CsvBindByPosition(position = 4)
  private String tourScheduleInformation;
}
