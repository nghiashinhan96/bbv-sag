package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AadAccountsSearchResultDto implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;
  private String firstName;
  private String lastName;
  private String primaryContactEmail;
  private String personalNumber;
  private String gender;
  private String legalEntityId;
}
