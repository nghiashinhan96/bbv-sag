package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.io.Serializable;

@Data
public class CsvMigrationEshopUser implements Serializable {

  private static final long serialVersionUID = -6432600255055612624L;

  @CsvBindByName(column = "ID", locale = "US")
  private Long id;

  @CsvBindByName(column = "USER_ID", locale = "US")
  private Long userId;

  @CsvBindByName(column = "AGE", locale = "US")
  private Integer age;

  @CsvBindByName(column = "EMAIL")
  private String email;

  @CsvBindByName(column = "EMAIL_CONFIRMATION")
  private Boolean emailConfirmation;

  @CsvBindByName(column = "FIRST_NAME")
  private String firstName;

  @CsvBindByName(column = "HOURLY_RATE", locale = "US")
  private Integer hourlyRate;

  @CsvBindByName(column = "LANGUAGE", locale = "US")
  private Integer language;

  @CsvBindByName(column = "LAST_NAME")
  private String lastName;

  @CsvBindByName(column = "NEWSLETTER")
  private Boolean newsletter;

  @CsvBindByName(column = "PHONE")
  private String phone;

  @CsvBindByName(column = "SALUTATION", locale = "US")
  private Integer salutation;

  @CsvBindByName(column = "SETTING", locale = "US")
  private Integer setting;

  @CsvBindByName(column = "TYPE")
  private String type;

  @CsvBindByName(column = "USERNAME")
  private String username;

  @CsvBindByName(column = "VAT_CONFIRM")
  private Boolean vatConfirm;

  @CsvBindByName(column = "ORGANISATION_ID", locale = "US")
  private Long orgId;

  @CsvBindByName(column = "PASSWORD")
  private String passWord;

  @CsvBindByName(column = "USER_ROLE")
  private String userRole;

  @CsvBindByName(column = "NEW_USER_ID", locale = "US")
  private Long newUserId;

  @CsvBindByName(column = "IS_MIGRATED")
  private Boolean isMigrated;

  @CsvBindByName(column = "NET_PRICE_VIEW")
  private Boolean netPriceView;

  @CsvBindByName(column = "NET_PRICE_CONFIRM")
  private Boolean netPriceConfirm;

  @CsvBindByName(column = "DEFAULT_SETTING", locale = "US")
  private Integer defaultSetting;

  @CsvBindByName(column = "HAS_PARTNER_PROGRAM")
  private Boolean hasPartnerProgram;

  @CsvBindByName(column = "HAS_PARTNER_PROGRAM_LOGIN")
  private Boolean hasPartnerProgramLogin;

}
