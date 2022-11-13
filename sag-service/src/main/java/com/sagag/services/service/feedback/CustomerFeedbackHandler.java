package com.sagag.services.service.feedback;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.service.dto.feedback.FeedBackCustomerUserDataDto;
import com.sagag.services.service.feedback.mail.FeedbackSendingEmailProcessor;
import com.sagag.services.service.feedback.mail.CustomerFeedbackSendingEmailProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerFeedbackHandler extends AbstractFeedbackHandler<FeedBackCustomerUserDataDto>
    implements CustomerUserDataHandler {

  @Autowired
  private CustomerFeedbackSendingEmailProcessor customerFeedbackSendingEmailProcessor;

  @Override
  protected FeedbackSendingEmailProcessor getSendingEmailProcessor() {
    return customerFeedbackSendingEmailProcessor;
  }

  @Override
  protected FeedBackCustomerUserDataDto getUserData(UserInfo user) {
    return getCustomerUserData(user);
  }

}

