package com.sagag.services.service.mail;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;

import java.util.Objects;

@Component
@Slf4j
public class ChangePasswordMailSender extends MailSender<ChangePasswordCriteria> {

  private static final String SUBJECT_FORGOT_PASSWORD_CODE = "mail.forgot_password.subject";

  private static final String SUBJECT_DIGI_INVOICE_REQUEST_CODE = "mail.digi-invoice.submission_request_email.subject";

  private static final String SECURITY_CODE_EMAIL = "email/get-new-security-code-template";

  private static final String SUCCESSFUL_RESET_PASS_TEMPLATE =
      "email/successful-reset-password-template";

  private static final String CHANGE_PASS_EMAIL_BY_HIMSELF = "email/get-new-password-template";

  private static final String CHANGE_PASS_EMAIL_BY_ADMIN =
      "email/get-new-password-template-by-admin";


  @Override
  public void send(ChangePasswordCriteria criteria) {

    Assert.hasText(criteria.getToEmail(), "The given toEmail must not be empty");
    Assert.hasText(criteria.getUsername(), "The given username must not be empty");
    Assert.hasText(criteria.getAffiliateEmail(), "The given affiliate email must not be empty");
    Assert.notNull(criteria.getLocale(), "The given locale must not be null");

    String templateName =
        criteria.isUpdatedByAdmin() ? CHANGE_PASS_EMAIL_BY_ADMIN : CHANGE_PASS_EMAIL_BY_HIMSELF;
    final Context context = new Context(criteria.getLocale());
    context.setVariable("username", criteria.getUsername());
    if (!criteria.isUpdatedByAdmin()) {
      context.setVariable("redirectUrl", criteria.getRedirectUrl());
    }
    context.setVariable("isFinalUser", criteria.isFinalUser());
    context.setVariable("isDigiInvoiceRequest", criteria.isDigiInvoiceRequest());

    if (Objects.nonNull(criteria.getCode())) {
      log.debug("Security Code is {}", criteria.getCode());
      context.setVariable("code", criteria.getCode());
      templateName = SECURITY_CODE_EMAIL;
    } else if (Boolean.TRUE.equals(criteria.isChangePassOk())) {
      // For reset password or change password in user profile
      log.debug("SUCCESSFUL_RESET_PASS_TEMPLATE is used");
      templateName = SUCCESSFUL_RESET_PASS_TEMPLATE;
    }

    final String subject =
        messageSource.getMessage(criteria.isDigiInvoiceRequest() ? SUBJECT_DIGI_INVOICE_REQUEST_CODE :
                SUBJECT_FORGOT_PASSWORD_CODE, null, criteria.getLocale());
    final String body = templateEngine.process(templateName, context);
    mailService.sendEmail(criteria.getAffiliateEmail(), criteria.getToEmail(), subject, body, true);
  }

}
