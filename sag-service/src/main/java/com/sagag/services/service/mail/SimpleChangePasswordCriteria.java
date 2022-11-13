package com.sagag.services.service.mail;

import lombok.Builder;
import lombok.Data;

import java.util.Locale;

@Data
@Builder
public class SimpleChangePasswordCriteria {

  private String toEmail;

  private String affiliateEmail;

  private String username;

  private String rawPassword;

  private String redirectUrl;

  private Locale locale;
}
