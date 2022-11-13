package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.sagag.services.tools.domain.SourceBaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CsvAddress extends SourceBaseObject {

  private static final long serialVersionUID = 9163612679485349921L;

  @CsvBindByName(column = "ID", locale = "US")
  private Long id;

  @CsvBindByName(column = "LINE1", locale = "US")
  private String line1;

  @CsvBindByName(column = "LINE2", locale = "US")
  private String line2;

  @CsvBindByName(column = "LINE3", locale = "US")
  private String line3;

  @CsvBindByName(column = "COUNTRYISO", locale = "US")
  private String countryIso;

  @CsvBindByName(column = "STATE", locale = "US")
  private String state;

  @CsvBindByName(column = "CITY", locale = "US")
  private String city;

  @CsvBindByName(column = "ZIPCODE", locale = "US")
  private String zipCode;

  @CsvBindByName(column = "TYPE", locale = "US")
  private String type;

  @CsvBindByName(column = "ERPID", locale = "US")
  private Long erpId;

  @CsvBindByName(column = "POBOX", locale = "US")
  private String poBox;

}
