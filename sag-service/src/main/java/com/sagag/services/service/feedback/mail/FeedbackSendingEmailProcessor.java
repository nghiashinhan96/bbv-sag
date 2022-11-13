package com.sagag.services.service.feedback.mail;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.service.request.FeedbackMessageRequest;

import java.util.Locale;

public interface FeedbackSendingEmailProcessor {

  /**
   * Processes input then sending the emails when create feedback successfully.
   *
   * @param user
   * @param topicId
   * @param createdFeedbackId
   * @param request
   * @param locale
   * @param salesEmail
   */
  void processThenSendingEmail(UserInfo user, int topicId,
                               Long createdFeedbackId, FeedbackMessageRequest request, Locale locale, String salesEmail)
      throws UserValidationException;
}
