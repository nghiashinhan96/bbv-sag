package com.sagag.services.service.feedback.mail;

import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.api.feedback.FeedbackDepartmentContactRepository;
import com.sagag.eshop.repo.enums.FeedbackDepartmentContactType;
import com.sagag.eshop.service.api.CountryService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.service.mail.feedback.FeedbackMessageCriteria;
import com.sagag.services.service.mail.feedback.FeedbackMessageMailSenderFactory;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Locale;

@Slf4j
@Component
public abstract class AbstractFeedbackSendingEmailProcessor
    implements FeedbackSendingEmailProcessor {

  private static final String DEFAULT_AFFILIATE_EMAIL_FOR_SALES_AGENT = "no-reply@sag-ag.ch";
  private static final String SAG_SHORT_NAME = "sag";
  protected static final String CH_COUNTRY_CODE = "CHE";

  @Autowired
  protected FeedbackDepartmentContactRepository feedbackDepartmentContactRepo;

  @Autowired
  protected SupportedAffiliateRepository supportedAffiliateRepo;

  @Autowired
  protected FeedbackMessageMailSenderFactory feedbackMessageMailSenderFactory;

  @Autowired
  protected CountryService countryService;

  @Autowired
  protected MessageSource messageSource;

  protected abstract void sendEmailToRecipient(FeedbackMessageCriteria criteria)
      throws UserValidationException;

  protected abstract void sendEmailConfirmationBackToSender(FeedbackMessageCriteria criteria)
      throws UserValidationException;

  protected void handleSendMailFail(String message, MessagingException ex)
      throws UserValidationException {
    log.error(message, ex);
    throw new UserValidationException(UserErrorCase.UE_SMF_001, message);
  }

  protected String getDepartmentEmail(int topicId, String affiliateShortname) {
    if (StringUtils.isNotBlank(affiliateShortname)) {
      return getDepartmentEmailForCustomer(topicId, affiliateShortname);
    }
    return getDepartmentEmailForSales(topicId);
  }

  private String getDepartmentEmailForCustomer(int topicId, String affiliateShortname) {
    String email = feedbackDepartmentContactRepo.findContactByTopicIdAndAffiliateShortName(topicId,
        affiliateShortname, FeedbackDepartmentContactType.EMAIL);
    if (StringUtils.isEmpty(email)) {
      email = feedbackDepartmentContactRepo.findContactCrossAffiliateByTopicId(topicId,
          FeedbackDepartmentContactType.EMAIL);
    }
    return email;
  }

  private String getDepartmentEmailForSales(int topicId) {
    return feedbackDepartmentContactRepo.findContactCrossAffiliateByTopicId(topicId,
        FeedbackDepartmentContactType.EMAIL);
  }

  protected String getAffiliateEmail(String affiliateShortname) {
    if (StringUtils.isBlank(affiliateShortname) || SAG_SHORT_NAME.equals(affiliateShortname)) {
      return getAffiliateEmailForSales();
    }
    return getAffiliateEmailForCustomer(affiliateShortname);
  }

  private String getAffiliateEmailForCustomer(String affiliateShortname) {
    return supportedAffiliateRepo.findNoReplyEmailByShortName(affiliateShortname)
        .orElse(StringUtils.EMPTY);
  }

  private String getAffiliateEmailForSales() {
    return DEFAULT_AFFILIATE_EMAIL_FOR_SALES_AGENT;
  }

  protected String getBranchEmailByBranchNumber(int branchNumber) {
    return feedbackDepartmentContactRepo.findBranchEmailByBranchNumber(branchNumber);
  }

  protected String getMessage(String s, Object[] object, Locale locale) {
    return messageSource.getMessage(s, object, locale);
  }
}
