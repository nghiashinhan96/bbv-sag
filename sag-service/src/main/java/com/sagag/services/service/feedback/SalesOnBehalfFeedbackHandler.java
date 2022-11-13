package com.sagag.services.service.feedback;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.service.dto.feedback.FeedBackCustomerUserDataDto;
import com.sagag.services.service.dto.feedback.FeedBackSalesOnBehalfUserDataDto;
import com.sagag.services.service.feedback.mail.FeedbackSendingEmailProcessor;
import com.sagag.services.service.feedback.mail.SalesOnBehalfFeedbackSendingEmailProcessor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalesOnBehalfFeedbackHandler extends
    AbstractFeedbackHandler<FeedBackSalesOnBehalfUserDataDto> implements CustomerUserDataHandler {

  @Autowired
  private SalesOnBehalfFeedbackSendingEmailProcessor salesOnBehalfFeedbackSendingEmailProcessor;

  @Override
  public FeedBackSalesOnBehalfUserDataDto getUserData(UserInfo user) {
    FeedBackCustomerUserDataDto customerUserData = getCustomerUserData(user);
    FeedBackSalesOnBehalfUserDataDto salesUserData =
        SagBeanUtils.map(customerUserData, FeedBackSalesOnBehalfUserDataDto.class);
    salesUserData.setSalesId(user.getSalesId());
    String salesEmail =
        eshopUserRepository.findEmailById(user.getSalesId()).orElse(StringUtils.EMPTY);
    salesUserData.setSalesEmail(salesEmail);
    return salesUserData;
  }

  @Override
  protected FeedbackSendingEmailProcessor getSendingEmailProcessor() {
    return salesOnBehalfFeedbackSendingEmailProcessor;
  }
}
