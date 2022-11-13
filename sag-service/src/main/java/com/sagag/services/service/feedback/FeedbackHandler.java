package com.sagag.services.service.feedback;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.service.request.FeedbackMessageRequest;
import com.sagag.services.service.resource.FeedbackMasterDataResource;

public interface FeedbackHandler<T> {

  /**
   * Creates feedback entry in DB and sending email.
   *
   * @param userInfo
   * @param request
   */
  void create(UserInfo userInfo, FeedbackMessageRequest request) throws UserValidationException;

  /**
   * Returns user data need to create feedback
   *
   * @param userInfo
   * @return
   */
  FeedbackMasterDataResource<T> getFeedbackUserData(UserInfo userInfo);
}
