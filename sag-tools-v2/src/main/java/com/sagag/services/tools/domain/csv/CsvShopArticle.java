package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CsvShopArticle implements Serializable {

  private static final long serialVersionUID = 7963385117204236149L;

  @CsvBindByName(column = "ID", locale = "US")
  private Long id;

  @CsvBindByName(column = "ORGANISATION_ID", locale = "US")
  private Long organisationId;

  @CsvBindByName(column = "TYPE")
  private String type;

  @CsvBindByName(column = "ARTICLENUMBER")
  private String articleNumber;

  @CsvBindByName(column = "NAME")
  private String name;

  @CsvBindByName(column = "DESCRIPTION", locale = "DE")
  private String description;

  @CsvBindByName(column = "AMOUNT", locale = "US")
  private Double amount;

  @CsvBindByName(column = "PRICE", locale = "US")
  private Double price;

  @CsvBindByName(column = "UC_ID", locale = "US")
  private Long userCreateId;

  @CsvBindByName(column = "DC", locale = "US")
  @CsvDate("yyyy-MM-dd HH:mm:ss")
  private Date dateCreate;

  @CsvBindByName(column = "UM_ID", locale = "US")
  private Long userModifyId;

  @CsvBindByName(column = "DM", locale = "US")
  @CsvDate("yyyy-MM-dd HH:mm:ss")
  private Date dateModify;

  @CsvBindByName(column = "VERSION", locale = "US")
  private Long version;

  @CsvBindByName(column = "TECSTATE")
  private String tecState;

  @CsvBindByName(column = "CURRENCYISO")
  private String currencyISO;
}
