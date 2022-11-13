package com.sagag.services.domain.eshop.criteria;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AadAccountsSearchRequestCriteria implements Serializable {

  private static final long serialVersionUID = 3314860769704937122L;
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
