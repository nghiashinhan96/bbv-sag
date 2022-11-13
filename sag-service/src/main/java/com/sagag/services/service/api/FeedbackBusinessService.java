package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.service.dto.feedback.FeedBackCustomerUserDataDto;
import com.sagag.services.service.dto.feedback.FeedBackSalesNotOnBehalfUserDataDto;
import com.sagag.services.service.dto.feedback.FeedBackSalesOnBehalfUserDataDto;
import com.sagag.services.service.request.FeedbackMessageRequest;
import com.sagag.services.service.resource.FeedbackMasterDataResource;

/**
 * Interface of Feedback business services.
 */
public interface FeedbackBusinessService {

  /**
   * Returns customer feedback entry in DB and sending email.
   *
   * @param userInfo
   * @param request
   * @throws UserValidationException
   */
  void createCustomerFeedback(UserInfo userInfo, FeedbackMessageRequest request)
      throws UserValidationException;

  /**
   * Returns sales on behalf feedback entry in DB and sending email.
   *
   * @param userInfo
   * @param request
   * @throws UserValidationException
   */
  void createSalesOnBehalfFeedback(UserInfo userInfo, FeedbackMessageRequest request)
      throws UserValidationException;

  /**
   * Returns sales not on behalf feedback entry in DB and sending email.
   *
   * @param userInfo
   * @param request
   * @throws UserValidationException
   */
  void createSalesNotOnBehalfFeedback(UserInfo userInfo, FeedbackMessageRequest request)
      throws UserValidationException;

  /**
   * Returns customer user data need for creating feedback.
   *
   * @param userInfo
   * @return user data
   */
  FeedbackMasterDataResource<FeedBackCustomerUserDataDto> getFeedbackCustomerUserData(
      UserInfo userInfo);

  /**
   * Returns sales on behalf user data need for creating feedback.
   *
   * @param userInfo
   * @return
   */
  FeedbackMasterDataResource<FeedBackSalesOnBehalfUserDataDto> getFeedbackSalesOnBehalfUserData(
      UserInfo userInfo);

  /**
   * Returns sales not on behalf user data need for creating feedback.
   *
   * @param userInfo
   * @return
   */
  FeedbackMasterDataResource<FeedBackSalesNotOnBehalfUserDataDto> getFeedbackSalesNotOnBehalfUserData(
      UserInfo userInfo);

}
