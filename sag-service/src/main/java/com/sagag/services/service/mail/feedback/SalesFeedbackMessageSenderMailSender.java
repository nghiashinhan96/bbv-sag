package com.sagag.services.service.mail.feedback;

import org.springframework.stereotype.Component;

@Component
public class SalesFeedbackMessageSenderMailSender extends AbstractFeedbackMessageMailSender {

  @Override
  protected String getEmailSubjectTemplate() {
    return "mail.sales.confirm.feedback.subject";
  }

  @Override
  protected String getEmailBodyTemplate() {
    return "email/sales_send_feedback_confirmation_back_to_sender";
  }
}
