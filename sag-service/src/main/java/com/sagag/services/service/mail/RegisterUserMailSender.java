package com.sagag.services.service.mail;

import com.sagag.services.common.contants.SagConstants;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RegisterUserMailSender extends MailSender<RegisterAccountCriteria> {

  @Autowired
  @Qualifier("scheduledExecutorService")
  private ScheduledExecutorService scheduledExecutorService;

  @Override
  public void send(RegisterAccountCriteria criteria) {

    final Locale locale = criteria.getLocale();
    final Context context = new Context(locale);
    context.setVariable("username", criteria.getUsername());
    context.setVariable("redirectUrl", criteria.getAccessUrl());
    context.setVariable("isFinalUser", criteria.getIsFinalUser());
    String shopName = criteria.getCompanyName();
    if (Boolean.TRUE.equals(criteria.getIsFinalUser())) {
      shopName = messageSource.getMessage("mail.order_confirm.automotive", null, locale);
    }
    String[] subjectParams = { shopName };
    final String subject =
        messageSource.getMessage("mail.create.user.subject", subjectParams, locale);
    final String body = templateEngine.process("email/create-new-user-template", context);

    mailService.sendEmail(criteria.getAffiliateEmail(), criteria.getEmail(), subject, body, true);
  }

  public void createSendConfirmEmailJob(RegisterAccountCriteria criteria, boolean isDvseCustomer) {
    if (!isDvseCustomer) {
      send(criteria);
      return;
    }
    CompletableFuture.supplyAsync(() -> {
      log.info("Add send email in scheduled job");
      scheduledExecutorService.schedule(() -> send(criteria),
          SagConstants.DELAY_MINUTE_TO_SEND_ACTIVE_USER_MAIL, TimeUnit.MINUTES);
      return null;
    });
  }

}
