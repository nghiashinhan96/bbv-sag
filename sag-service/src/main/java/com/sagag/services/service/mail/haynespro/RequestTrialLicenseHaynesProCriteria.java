package com.sagag.services.service.mail.haynespro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestTrialLicenseHaynesProCriteria {

  private String affiliateEmail;

  private String customerNr;

  private String username;

  private String email;

  private String receiptEmail;

  private Locale locale;

  private String requestType;

}
