package com.sagag.services.service.mail.registration;

import com.sagag.services.service.mail.MailSender;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@Slf4j
public class PotentialCustomerRegistrationMailSender
    extends MailSender<PotentialCustomerRegistrationCriteria> {

  @Override
  public void send(PotentialCustomerRegistrationCriteria criteria) {
    log.debug("Sending register potential customer email with criteria = {}", criteria);

    final Context context = new Context(criteria.getLocale());
    context.setVariable("data", criteria);
    final String subject = messageSource.getMessage("mail.registration.potentialcustomer.subject",
        null, criteria.getLocale());
    final String body =
        templateEngine.process("email/register-new-potential-customer-template", context);

    mailService.sendEmail(criteria.getFromEmail(), criteria.getToEmail(), subject, body, true);
  }
}
