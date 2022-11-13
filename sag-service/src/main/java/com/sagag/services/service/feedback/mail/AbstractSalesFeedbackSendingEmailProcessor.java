package com.sagag.services.service.feedback.mail;

import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.service.mail.feedback.FeedbackMessageCriteria;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;

public abstract class AbstractSalesFeedbackSendingEmailProcessor
    extends AbstractFeedbackSendingEmailProcessor {

  private static final String SAG_COLLECTION_SHORTNAME = "sag";

  private static final String SEND_FEEDBACK_NOTIFICATION_EMAIL =
      SettingsKeys.Affiliate.Settings.SEND_FEEDBACK_NOTIFICATION_EMAIL.toLowerName();

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Override
  protected void sendEmailToRecipient(FeedbackMessageCriteria criteria)
      throws UserValidationException {
    try {
      feedbackMessageMailSenderFactory.salesSendMailToRecipient(criteria);
    } catch (MessagingException ex) {
      handleSendMailFail("Fail to send email to department", ex);
    }
  }

  @Override
  protected void sendEmailConfirmationBackToSender(FeedbackMessageCriteria criteria)
      throws UserValidationException {
    try {
      feedbackMessageMailSenderFactory.salesSendMailConfirmationBackToSender(criteria);
    } catch (MessagingException ex) {
      handleSendMailFail("Fail to send email back to sales", ex);
    }
  }

  protected boolean shouldSendNotificationEmail() {
    String sendFeedbackNotification =
        orgCollectionService.findSettingValueByCollectionShortnameAndKey(
            SAG_COLLECTION_SHORTNAME, SEND_FEEDBACK_NOTIFICATION_EMAIL).orElse(StringUtils.EMPTY);
    return new Boolean(sendFeedbackNotification);
  }
}
