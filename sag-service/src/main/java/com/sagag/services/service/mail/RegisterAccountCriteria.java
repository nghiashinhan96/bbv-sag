package com.sagag.services.service.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAccountCriteria {

  private String email;

  private String username;

  private String affiliateEmail;

  private String accessUrl;

  private Locale locale;

  private String companyName;

  private Boolean isFinalUser;

}
