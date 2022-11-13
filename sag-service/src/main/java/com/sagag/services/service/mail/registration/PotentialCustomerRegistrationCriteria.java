package com.sagag.services.service.mail.registration;

import com.sagag.services.service.dto.registration.PotentialCustomerRegistrationDto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@Data
@RequiredArgsConstructor(staticName = "of")
public class PotentialCustomerRegistrationCriteria {

  @NonNull
  private String toEmail;

  @NonNull
  private String fromEmail;

  @NonNull
  private PotentialCustomerRegistrationDto info;

  @NonNull
  private Locale locale;
}
