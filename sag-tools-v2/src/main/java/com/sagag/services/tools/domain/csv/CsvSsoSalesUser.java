package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

import java.io.Serializable;

@Data
public class CsvSsoSalesUser implements Serializable {

  private static final long serialVersionUID = 1318655015304007509L;

  @CsvBindByName(column = "USERNAME")
  private String userName;

  @CsvBindByName(column = "SUR_NAME")
  private String surName;

  @CsvBindByName(column = "FIRST_NAME")
  private String firstName;

  @CsvBindByName(column = "EMAIL")
  private String email;

  @CsvBindByName(column = "PERSONAL_NUMBER")
  private String personalNumber;

  @CsvBindByName(column = "LEGAL_ENTITY_ID")
  private String legalEntityId;
}
