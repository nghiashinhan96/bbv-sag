package com.sagag.services.service.mail.feedback;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public class FeedbackMessageMailSenderFactory {

  @Autowired
  private FeedbackMessageRecipientMailSender feedbackMessageRecipientMailSender;

  @Autowired
  private FeedbackMessageSenderMailSender feedbackMessageSenderMailSender;

  @Autowired
  private SalesFeedbackMessageRecipientMailSender salesFeedbackMessageRecipientMailSender;

  @Autowired
  private SalesFeedbackMessageSenderMailSender salesFeedbackMessageSenderMailSender;

  public void sendMailToRecipient(FeedbackMessageCriteria criteria) throws MessagingException {
    feedbackMessageRecipientMailSender.send(criteria);
  }

  public void sendMailConfirmationBackToSender(FeedbackMessageCriteria criteria)
      throws MessagingException {
    feedbackMessageSenderMailSender.send(criteria);
  }

  public void salesSendMailToRecipient(FeedbackMessageCriteria criteria) throws MessagingException {
    salesFeedbackMessageRecipientMailSender.send(criteria);
  }

  public void salesSendMailConfirmationBackToSender(FeedbackMessageCriteria criteria)
      throws MessagingException {
    salesFeedbackMessageSenderMailSender.send(criteria);
  }
}
