package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.io.Serializable;

@Data
public class CsvRoleAssignment implements Serializable {

  private static final long serialVersionUID = -5912588633772267252L;

  @CsvBindByName(column = "ROLE", locale = "US")
  private String role;

  @CsvBindByName(column = "PERSON_ID", locale = "US")
  private Long personId;

  @CsvBindByName(column = "ORGANISATION_ID", locale = "US")
  private Long orgId;

  @CsvBindByName(column = "LOGIN_ID", locale = "US")
  private Long loginId;

}
