package com.sagag.eshop.repo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.contants.SagConstants;

import lombok.Data;

import java.io.Serializable;

/**
 * Transfer object data from csv file.
 */
@Data
public class CsvAadAccount implements Serializable {

  private static final long serialVersionUID = 4774095787330690177L;

  @JsonProperty("FIRSTNAME")
  private final String firstName;
  @JsonProperty("LASTNAME")
  private final String lastName;
  @JsonProperty("PRIMARYCONTACTEMAIL")
  private final String primaryContactEmail;
  @JsonProperty("PERSONNELNUMBER")
  private final Integer personalNumber;
  @JsonProperty("Gender")
  private final String gender;
  @JsonProperty("EMPLOYMENTLEGALENTITYID")
  private final String legalEntityId;
  private String permitGroup;

  @JsonCreator
  public CsvAadAccount(@JsonProperty("FIRSTNAME") final String firstName,
      @JsonProperty("LASTNAME") final String lastName,
      @JsonProperty("PRIMARYCONTACTEMAIL") final String primaryContactEmail,
      @JsonProperty("PERSONNELNUMBER") final Integer personalNumber,
      @JsonProperty("Gender") final String gender,
      @JsonProperty("EMPLOYMENTLEGALENTITYID") final String legalEntityId) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.primaryContactEmail = primaryContactEmail;
    this.personalNumber = personalNumber;
    this.gender = gender;
    this.legalEntityId = legalEntityId;

  }
  
  public String toInsertScript() {
    final StringBuilder insertScript = new StringBuilder(
        "INSERT INTO [dbo].[AAD_ACCOUNTS] (FIRST_NAME, LAST_NAME, PRIMARY_CONTACT_EMAIL, PERSONAL_NUMBER, GENDER, LEGAL_ENTITY_ID, PERMIT_GROUP) VALUES (")
    .append("\'").append(this.firstName).append("\'")
    .append(SagConstants.COMMA).append("\'").append(this.lastName).append("\'")
    .append(SagConstants.COMMA).append("\'").append(this.primaryContactEmail).append("\'")
    .append(SagConstants.COMMA).append(this.personalNumber)
    .append(SagConstants.COMMA).append("\'").append(this.gender).append("\'")
    .append(SagConstants.COMMA).append("\'").append(this.legalEntityId).append("\'")
    .append(SagConstants.COMMA).append("\'").append(this.permitGroup).append("\'")
    .append(");");
    return insertScript.toString();
  }
  
  @Override
  public String toString() {
    return toInsertScript();
  }
}
