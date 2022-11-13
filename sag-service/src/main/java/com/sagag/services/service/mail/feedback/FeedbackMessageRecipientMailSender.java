package com.sagag.services.service.mail.feedback;

import org.springframework.stereotype.Component;

@Component
public class FeedbackMessageRecipientMailSender extends AbstractFeedbackMessageMailSender {

  @Override
  protected String getEmailSubjectTemplate() {
    return "mail.sales.feedback.subject";
  }

  @Override
  protected String getEmailBodyTemplate() {
    return "email/send_feedback_to_recipient";
  }
}
