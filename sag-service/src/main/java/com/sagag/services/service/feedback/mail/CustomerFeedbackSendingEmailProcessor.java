package com.sagag.services.service.feedback.mail;

import static org.apache.commons.lang3.StringUtils.defaultString;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.service.dto.feedback.FeedbackDataItem;
import com.sagag.services.service.dto.feedback.FeedbackParentDataItem;
import com.sagag.services.service.mail.feedback.FeedbackMessageCriteria;
import com.sagag.services.service.request.FeedbackMessageRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

@Component
public class CustomerFeedbackSendingEmailProcessor extends AbstractFeedbackSendingEmailProcessor {

  private static final String NAME = "Name";

  private static final String USER_NAME = "Username";

  private static final String EMAIL = "E-Mail";

  @Autowired
  protected EmailValidator emailValidator;

  @Override
  public void processThenSendingEmail(UserInfo user, int topicId, Long createdFeedbackId, FeedbackMessageRequest request,
                                      Locale locale, String salesEmail) throws UserValidationException {

    String iso3Country = countryService.searchDefaultLocaleByAffiliate(user.getAffiliateShortName()).getISO3Country();
    FeedbackParentDataItem userInfo = convertUserInfoToFeedbackParentDataItem(user, locale);

    FeedbackMessageCriteria emailToRecipientCriteria;
    // 7772 [CH only] Customer feedbacks from customer view must be sent to the primary_email of the customer's default branch
    final String departmentEmail = getDepartmentEmail(topicId, user.getAffiliateShortName());
    if(!StringUtils.isEmpty(iso3Country) && iso3Country.equalsIgnoreCase(CH_COUNTRY_CODE)) {
      //@formatter:off
      String branchEmail = getBranchEmailByBranchNumber(Integer.parseInt(user.getDefaultBranchId()));
      if (StringUtils.isBlank(branchEmail) ||!emailValidator.isValid(branchEmail, null)) {
        branchEmail = departmentEmail;
      }
      emailToRecipientCriteria = FeedbackMessageCriteria.builder()
              .fromEmail(getAffiliateEmail(user.getAffiliateShortName()))
              .toEmail(branchEmail)
              .ccEmail(new String[]{departmentEmail})
              .topic(request.getTopic())
              .defaultBranch(request.getAffiliateStore())
              .feedbackId(createdFeedbackId)
              .message(request.getMessage())
              .userData(request.getUserData())
              .userInfo(userInfo)
              .technicalData(request.getTechnicalData())
              .locale(locale)
              .source(request.getSource())
              .subjectParams(new String[]{iso3Country, StringUtils.defaultString(createdFeedbackId.toString()),
                      defaultString(request.getTopic().getTopic())})
              .attachments(request.getAttachments()).build();
      //@formatter:off
    } else {
      //@formatter:off
      emailToRecipientCriteria = FeedbackMessageCriteria.builder()
              .fromEmail(getAffiliateEmail(user.getAffiliateShortName()))
              .toEmail(departmentEmail)
              .topic(request.getTopic())
              .defaultBranch(request.getAffiliateStore())
              .feedbackId(createdFeedbackId)
              .message(request.getMessage())
              .userData(request.getUserData()).userInfo(userInfo)
              .technicalData(request.getTechnicalData())
              .locale(locale)
              .source(request.getSource())
              .subjectParams(new String[]{iso3Country, StringUtils.defaultString(createdFeedbackId.toString()),
                      defaultString(request.getTopic().getTopic())})
              .attachments(request.getAttachments()).build();
      //@formatter:off
    }

    sendEmailToRecipient(emailToRecipientCriteria);
    //@formatter:off
    FeedbackMessageCriteria emailConfirmationBackToSenderCriteria = FeedbackMessageCriteria
        .builder()
        .fromEmail(getAffiliateEmail(user.getAffiliateShortName()))
        .toEmail(user.getEmail())
        .topic(request.getTopic())
        .defaultBranch(request.getAffiliateStore())
        .feedbackId(createdFeedbackId)
        .message(request.getMessage())
        .userData(request.getUserData()).userInfo(userInfo)
        .technicalData(request.getTechnicalData().getShortTechnicalData())
        .locale(locale)
        .source(request.getSource())
        .attachments(request.getAttachments())
        .subjectParams(new String[]{ StringUtils.defaultString(createdFeedbackId.toString()),
                defaultString(request.getTopic().getTopic()) })
        .build();
    //@formatter:on
    this.sendEmailConfirmationBackToSender(emailConfirmationBackToSenderCriteria);
  }


  @Override
  protected void sendEmailToRecipient(FeedbackMessageCriteria criteria)
      throws UserValidationException {
    try {
      feedbackMessageMailSenderFactory.sendMailToRecipient(criteria);
    } catch (MessagingException ex) {
      handleSendMailFail("Fail to send email to department", ex);
    }
  }

  @Override
  protected void sendEmailConfirmationBackToSender(FeedbackMessageCriteria criteria)
      throws UserValidationException {
    try {
      feedbackMessageMailSenderFactory.sendMailConfirmationBackToSender(criteria);
    } catch (MessagingException ex) {
      handleSendMailFail("Fail to send email back to customer or sales", ex);
    }
  }

  private FeedbackParentDataItem convertUserInfoToFeedbackParentDataItem(UserInfo user, Locale locale) {
    FeedbackParentDataItem userDataItem = new FeedbackParentDataItem();
    userDataItem.setTitle(getMessage("mail.feedback.user.title", null, locale));

    FeedbackDataItem name = new FeedbackDataItem();
    name.setTitle(NAME);
    name.setValue(user.getFirstName() + " " + user.getLastName());

    FeedbackDataItem userName = new FeedbackDataItem();
    userName.setTitle(USER_NAME);
    userName.setValue(user.getUsername());

    FeedbackDataItem email = new FeedbackDataItem();
    email.setTitle(EMAIL);
    email.setValue(user.getEmail());

    List<FeedbackDataItem> userItems = Arrays.asList(name, userName, email);
    userDataItem.setItems(userItems);
    return userDataItem;
  }
}
