package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

import java.io.Serializable;

@Data
public class CsvPartnerProgramSetting implements Serializable {

  private static final long serialVersionUID = -2037570258447667721L;

  @CsvBindByName(column = "ORG_CODE")
  private String orgCode;

  @CsvBindByName(column = "USERNAME")
  private String userName;

  @CsvBindByName(column = "PARTNER_PROGRAM_CUSTOMER")
  private Boolean customerSettingHasPartnerProgram;

  @CsvBindByName(column = "PARTNER_PROGRAM_USER")
  private Boolean userSettingHasPartnerProgram;

}
