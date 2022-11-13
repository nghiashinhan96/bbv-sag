package com.sagag.services.domain.eshop.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WssCsvOpeningCalendar implements Serializable {

  private static final long serialVersionUID = 9165541784771830247L;

  private Integer id;

  @CsvBindByName(column = "DATE", locale = "US", required = true)
  @CsvDate("yyyy-MM-dd")
  private Date date;

  @CsvBindByName(column = "WORKING_DAY_CODE", required = true)
  private String workingDayCode;

  @CsvBindByName(column = "COUNTRY", required = true)
  private String country;
}
