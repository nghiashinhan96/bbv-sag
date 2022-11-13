package com.sagag.services.service.mail.feedback;

import org.springframework.stereotype.Component;

@Component
public class FeedbackMessageSenderMailSender extends AbstractFeedbackMessageMailSender {

  @Override
  protected String getEmailSubjectTemplate() {
    return "mail.sales.confirm.feedback.subject";
  }

  @Override
  protected String getEmailBodyTemplate() {
    return "email/send_feedback_confirmation_back_to_sender";
  }
}
