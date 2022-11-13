package com.sagag.services.tools.domain;

import com.sagag.services.tools.domain.csv.CsvSsoSalesUser;
import com.sagag.services.tools.utils.SagBeanUtils;

import lombok.Data;


@Data
public class SsoSalesUser {

  private String userName;

  private String surName;

  private String firstName;

  private String email;

  private String personalNumber;

  private String legalEntityId;

  public static SsoSalesUser fromCsvSsoSalesUser(CsvSsoSalesUser csvSsoSalesUser) {
    return SagBeanUtils.map(csvSsoSalesUser, SsoSalesUser.class);
  }
}
