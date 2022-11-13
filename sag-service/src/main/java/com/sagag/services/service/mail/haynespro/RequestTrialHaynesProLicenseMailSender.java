package com.sagag.services.service.mail.haynespro;

import com.sagag.services.service.mail.MailSender;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.util.Locale;

import javax.mail.MessagingException;

@Component
public class RequestTrialHaynesProLicenseMailSender
    extends MailSender<RequestTrialLicenseHaynesProCriteria> {

  /**
   * Sends email to request 30-day test license for HaynesPro Ultimate for a customer.
   *
   * @param criteria the criteria info
   * @throws MessagingException
   */
  @Override
  public void send(final RequestTrialLicenseHaynesProCriteria criteria) throws MessagingException {

    final Locale locale = criteria.getLocale();
    final Context context = new Context(locale);
    context.setVariable("customerNr", criteria.getCustomerNr());
    context.setVariable("username", criteria.getUsername());
    context.setVariable("email", criteria.getEmail());

    final String subject = messageSource.getMessage("mail.request.hp.trial_license.subject", null,
        criteria.getLocale());
    final String body =
        templateEngine.process("email/request-trial-haynespro-license-template", context);
    mailService.sendEmail(criteria.getAffiliateEmail(), criteria.getReceiptEmail(),
        subject, body, true);
  }
}
