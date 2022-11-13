package com.sagag.services.service.mail;

import com.sagag.eshop.repo.entity.EshopUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordCriteria {

  private String toEmail;

  private String username;

  private String affiliateEmail;

  private String rawPassword;

  private String redirectUrl;

  private Locale locale;

  private String code; // the code to verify sent to user email

  private boolean changePassOk;

  private boolean isUpdatedByAdmin;

  private boolean isFinalUser;

  private boolean isDigiInvoiceRequest;

  public static ChangePasswordCriteria buildResetPaswordEmail(final EshopUser eshopUser,
      final String fromEmail, final String redirectUrl, final boolean changePassOk,
      final Locale locale) {
    return buildCriteria(eshopUser, fromEmail, eshopUser.getEmail(), redirectUrl, null,
            changePassOk, locale, false);
  }

  public static ChangePasswordCriteria buildSecurityCodeEmail(final EshopUser eshopUser,
      final String affiliateEmail, final String toEmail, final String redirectUrl, final String genCode,
      final Locale locale, final boolean isDigiInvoiceRequest) {
    return buildCriteria(eshopUser, affiliateEmail, toEmail, redirectUrl, genCode, false, locale,
            isDigiInvoiceRequest);
  }

  private static ChangePasswordCriteria buildCriteria(final EshopUser eshopUser,
      final String affiliateEmail, final String toEmail, final String redirectUrl, final String genCode,
      final boolean changePassOk, final Locale locale, final boolean isDigiInvoiceRequest) {
    return ChangePasswordCriteria.builder()
        .toEmail(toEmail)
        .affiliateEmail(affiliateEmail)
        .username(eshopUser.getUsername())
        .redirectUrl(redirectUrl)
        .code(genCode)
        .changePassOk(changePassOk)
        .locale(locale)
        .isDigiInvoiceRequest(isDigiInvoiceRequest)
        .build();
  }

}
