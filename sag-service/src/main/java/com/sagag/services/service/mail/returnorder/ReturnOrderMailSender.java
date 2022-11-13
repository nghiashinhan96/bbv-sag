package com.sagag.services.service.mail.returnorder;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.service.mail.MailSender;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Component
public class ReturnOrderMailSender extends MailSender<ReturnOrderCriteria> {

  private static final String SUBJECT_RETURN_ORDER = "mail.return-order.subject";

  private static final String SUBJECT_RETURN_ORDER_ERROR = "mail.return-order.error-subject";

  private static final String SUBJECT_RETURN_ORDER_GET_ORDER_NUMBER_ERROR = "mail.return-order.order-number-error-subject";

  private static final String SUBJECT_RETURN_ORDER_RUNNING = "mail.return-order.running-subject";

  private static final String RETURN_ORDER_ERROR_MESSAGE = "mail.return-order.error";

  private static final String RETURN_ORDER_RUNNING_MESSAGE = "mail.return-order.running";

  private static final String RETURN_ORDER_GET_ORDER_NUMBER_ERROR_MESSAGE = "mail.return-order.order-number-error";

  private static final String RETURN_ORDER_TEMPLATE = "email/return-order-template";

  private static final int MAX_SUBJECT_LENGTH = 150;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Override
  public void send(ReturnOrderCriteria criteria) {

    Assert.hasText(criteria.getToEmail(), "The given toEmail must not be empty");
    Assert.hasText(criteria.getAffiliateEmail(), "The given affiliate email must not be empty");
    final Locale locale = localeContextHelper.toLocale(criteria.getLangiso());
    Assert.notNull(locale, "The given locale must not be null");
    final String orderNumbers = StringUtils
        .join(CollectionUtils.emptyIfNull(criteria.getOrderNumbers()), SagConstants.COMMA);
    final Context context = new Context(locale);
    context.setVariable("username", criteria.getUsername());
    context.setVariable("orderNumbers", orderNumbers);
    context.setVariable("isError", criteria.isError());

    String subject = StringUtils.EMPTY;
    String errorMessage = StringUtils.EMPTY;
    if (!criteria.isError()) {
      subject = messageSource.getMessage(SUBJECT_RETURN_ORDER, null, locale);
      subject = StringUtils.join(subject, orderNumbers);
    } else {
      String[] errorParams = { criteria.getBatchJobId() };
      switch (criteria.getErrorType()) {
      case STATUS_CHECK_RETURN_ERROR:
        subject = messageSource.getMessage(SUBJECT_RETURN_ORDER_ERROR, null, locale);
        errorMessage = messageSource.getMessage(RETURN_ORDER_ERROR_MESSAGE, errorParams, locale);
        break;
      case STATUS_CHECK_TIME_OUT:
        subject = messageSource.getMessage(SUBJECT_RETURN_ORDER_RUNNING, null, locale);
        errorMessage = messageSource.getMessage(RETURN_ORDER_RUNNING_MESSAGE, errorParams, locale);
        break;
      case GET_RETURN_ORDER_ORDER_NUMBER_FAILED:
        subject = messageSource.getMessage(SUBJECT_RETURN_ORDER_GET_ORDER_NUMBER_ERROR, null, locale);
        errorMessage = messageSource.getMessage(RETURN_ORDER_GET_ORDER_NUMBER_ERROR_MESSAGE, errorParams, locale);
        break;
      default:
        break;
      }
    }
    if (StringUtils.length(subject) > MAX_SUBJECT_LENGTH) {
      subject = StringUtils.rightPad(StringUtils.left(subject, MAX_SUBJECT_LENGTH - 3),
          MAX_SUBJECT_LENGTH, SagConstants.DOT_CHAR);
    }
    context.setVariable("errorMessage", errorMessage);

    final String body = templateEngine.process(RETURN_ORDER_TEMPLATE, context);
    mailService.sendEmail(criteria.getAffiliateEmail(), criteria.getToEmail(), subject, body, true);
  }

}
