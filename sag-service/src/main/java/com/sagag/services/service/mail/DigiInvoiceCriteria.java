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
public class DigiInvoiceCriteria {

  private String affiliateEmail;

  private String invoiceRecipientEmail;

  private String invoiceRequestEmail;

  private Locale locale;

  private String customerAddress;

  public static DigiInvoiceCriteria buildCriteria(final String invoiceRecipientEmail,
      final Locale locale, final String affiliateEmail, final String invoiceRequestEmail,
      final String customerAddress) {
    return DigiInvoiceCriteria.builder().affiliateEmail(affiliateEmail)
        .customerAddress(customerAddress).invoiceRecipientEmail(invoiceRecipientEmail)
        .invoiceRequestEmail(invoiceRequestEmail).locale(locale).build();
  }
}
