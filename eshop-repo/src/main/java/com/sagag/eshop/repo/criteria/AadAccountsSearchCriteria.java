package com.sagag.eshop.repo.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AadAccountsSearchCriteria {
  private String firstName;
  private String lastName;
  private String primaryContactEmail;
  private String personalNumber;
  private String gender;
  private String legalEntityId;

  private Boolean orderDescByFirstName;
  private Boolean orderDescByLastName;
  private Boolean orderDescByPrimaryContactEmail;
  private Boolean orderDescByPersonalNumber;
  private Boolean orderDescByGender;
  private Boolean orderDescBylegalEntityId;
}
