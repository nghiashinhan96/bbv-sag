package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AadAccountsDto implements Serializable {

  private static final long serialVersionUID = 8285523538423316248L;

  private String firstName;
  private String lastName;
  private String primaryContactEmail;
  private String personalNumber;
  private String gender;
  private String legalEntityId;
}
