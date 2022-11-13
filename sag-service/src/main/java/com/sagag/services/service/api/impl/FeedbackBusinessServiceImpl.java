package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.service.api.FeedbackBusinessService;
import com.sagag.services.service.dto.feedback.FeedBackCustomerUserDataDto;
import com.sagag.services.service.dto.feedback.FeedBackSalesNotOnBehalfUserDataDto;
import com.sagag.services.service.dto.feedback.FeedBackSalesOnBehalfUserDataDto;
import com.sagag.services.service.feedback.CustomerFeedbackHandler;
import com.sagag.services.service.feedback.SalesNotOnBehalfFeedbackHandler;
import com.sagag.services.service.feedback.SalesOnBehalfFeedbackHandler;
import com.sagag.services.service.request.FeedbackMessageRequest;
import com.sagag.services.service.resource.FeedbackMasterDataResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FeedbackBusinessServiceImpl implements FeedbackBusinessService {

  @Autowired
  private CustomerFeedbackHandler customerFeedbackHandler;

  @Autowired
  private SalesNotOnBehalfFeedbackHandler salesNotOnBehalfFeedbackHandler;

  @Autowired
  private SalesOnBehalfFeedbackHandler salesOnBehalfFeedbackHandler;

  @Override
  public void createCustomerFeedback(UserInfo user, FeedbackMessageRequest request)
      throws UserValidationException {
    customerFeedbackHandler.create(user, request);
  }

  @Override
  public void createSalesOnBehalfFeedback(UserInfo user, FeedbackMessageRequest request)
      throws UserValidationException {
    salesOnBehalfFeedbackHandler.create(user, request);
  }

  @Override
  public void createSalesNotOnBehalfFeedback(UserInfo user, FeedbackMessageRequest request)
      throws UserValidationException {
    salesNotOnBehalfFeedbackHandler.create(user, request);
  }

  @Override
  public FeedbackMasterDataResource<FeedBackCustomerUserDataDto> getFeedbackCustomerUserData(
      UserInfo user) {
    return customerFeedbackHandler.getFeedbackUserData(user);
  }

  @Override
  public FeedbackMasterDataResource<FeedBackSalesOnBehalfUserDataDto> getFeedbackSalesOnBehalfUserData(
      UserInfo user) {
    return this.salesOnBehalfFeedbackHandler.getFeedbackUserData(user);
  }

  @Override
  public FeedbackMasterDataResource<FeedBackSalesNotOnBehalfUserDataDto> getFeedbackSalesNotOnBehalfUserData(
      UserInfo user) {
    return salesNotOnBehalfFeedbackHandler.getFeedbackUserData(user);
  }
}
