package com.sagag.services.service.mail.feedback;

import com.sagag.services.service.mail.MailSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;

@Slf4j
public abstract class AbstractFeedbackMessageMailSender
    extends MailSender<FeedbackMessageCriteria> {

  protected abstract String getEmailSubjectTemplate();

  protected abstract String getEmailBodyTemplate();

  @Override
  public void send(FeedbackMessageCriteria criteria) throws MessagingException {
    log.debug("Sending feedback message email with criteria = {}", criteria);
    validateCriteria(criteria);
    final Context context = new Context(criteria.getLocale());
    context.setVariable("data", criteria);
    final String body = templateEngine.process(getEmailBodyTemplate(), context);

    final String subject =
        messageSource.getMessage(getEmailSubjectTemplate(), criteria.getSubjectParams(), criteria.getLocale());

    if(!ArrayUtils.isEmpty(criteria.getCcEmail())) {
      mailService.sendEmailIncludeCc(criteria.getFromEmail(), criteria.getToEmail(), criteria.getCcEmail(),
              subject, body, true, criteria.getAttachments());
    } else {
      mailService.sendEmail(criteria.getFromEmail(), criteria.getToEmail(), subject, body, true,
              criteria.getAttachments());
    }
  }

  private void validateCriteria(FeedbackMessageCriteria criteria) {
    Assert.notNull(criteria.getTopic(), "Topic must not be null");
    Assert.notNull(criteria.getUserData(), "User data must not be null");
    Assert.notNull(criteria.getMessage(), "Message must not be null");
    Assert.notNull(criteria.getTechnicalData(), "Technical data must not be null");
    Assert.notNull(criteria.getSubjectParams(), "Message subject must not be empty");
  }
}
