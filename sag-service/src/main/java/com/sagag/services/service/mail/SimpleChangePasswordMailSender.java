package com.sagag.services.service.mail;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;

@Component
public class SimpleChangePasswordMailSender extends MailSender<SimpleChangePasswordCriteria> {

  @Override
  public void send(SimpleChangePasswordCriteria criteria) {
    Assert.hasText(criteria.getToEmail(), "The given toEmail must not be empty");
    Assert.hasText(criteria.getUsername(), "The given username must not be empty");
    Assert.hasText(criteria.getAffiliateEmail(), "The given affiliate email must not be empty");
    Assert.notNull(criteria.getLocale(), "The given locale must not be null");


    final Context context = new Context(criteria.getLocale());
    context.setVariable("username", criteria.getUsername());
    context.setVariable("redirectUrl", criteria.getRedirectUrl());
    context.setVariable("newPassword", criteria.getRawPassword());
    final String subject =
        messageSource.getMessage("mail.forgot_password.subject", null, criteria.getLocale());
    final String body = templateEngine.process("email/get-new-password-template", context);
    mailService.sendEmail(criteria.getAffiliateEmail(), criteria.getToEmail(), subject, body, true);
  }
}
