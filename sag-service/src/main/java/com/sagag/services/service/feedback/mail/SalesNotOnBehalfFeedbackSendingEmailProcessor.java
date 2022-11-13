package com.sagag.services.service.feedback.mail;

import static org.apache.commons.lang3.StringUtils.defaultString;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.service.mail.feedback.FeedbackMessageCriteria;
import com.sagag.services.service.request.FeedbackMessageRequest;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SalesNotOnBehalfFeedbackSendingEmailProcessor
    extends AbstractSalesFeedbackSendingEmailProcessor {

  @Override
  public void processThenSendingEmail(UserInfo user, int topicId,
                                      Long createdFeedbackId, FeedbackMessageRequest request, Locale locale, String salesEmail)
      throws UserValidationException {

    final String country = countryService.searchDefaultLocaleByAffiliate(user.getAffiliateShortName()).getISO3Country();

    //@formatter:off
    FeedbackMessageCriteria emailToRecipientCriteria = FeedbackMessageCriteria.builder()
        .fromEmail(salesEmail)
        .toEmail(getDepartmentEmail(topicId, null))
        .topic(request.getTopic())
        .defaultBranch(request.getAffiliateStore())
        .feedbackId(createdFeedbackId)
        .message(request.getMessage())
        .salesInfo(request.getSalesInfo())
        .userData(request.getUserData())
        .technicalData(request.getTechnicalData())
        .locale(locale)
        .source(request.getSource())
        .subjectParams(new String[]{ defaultString(country), defaultString(createdFeedbackId.toString()),
            defaultString(request.getTopic().getTopic()) })
        .attachments(request.getAttachments()).build();

    FeedbackMessageCriteria emailConfirmationBackToSenderCriteria = FeedbackMessageCriteria
        .builder()
        .fromEmail(getAffiliateEmail(null))
        .toEmail(salesEmail)
        .topic(request.getTopic())
        .defaultBranch(request.getAffiliateStore())
        .feedbackId(createdFeedbackId)
        .message(request.getMessage())
        .salesInfo(request.getSalesInfo())
        .userData(request.getUserData())
        .technicalData(request.getTechnicalData().getShortTechnicalData())
        .locale(locale)
        .source(request.getSource())
        .subjectParams(
            new String[]{ defaultString(createdFeedbackId.toString()), defaultString(request.getTopic().getTopic()) })
        .attachments(request.getAttachments()).build();
    //@formatter:on

    sendEmailToRecipient(emailToRecipientCriteria);
    if (shouldSendNotificationEmail()) {
      sendEmailConfirmationBackToSender(emailConfirmationBackToSenderCriteria);
    }
  }
}
