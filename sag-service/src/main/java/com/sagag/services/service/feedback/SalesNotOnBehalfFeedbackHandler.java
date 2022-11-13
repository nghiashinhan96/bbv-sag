package com.sagag.services.service.feedback;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.service.dto.feedback.FeedBackSalesNotOnBehalfUserDataDto;
import com.sagag.services.service.feedback.mail.FeedbackSendingEmailProcessor;
import com.sagag.services.service.feedback.mail.SalesNotOnBehalfFeedbackSendingEmailProcessor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalesNotOnBehalfFeedbackHandler
    extends AbstractFeedbackHandler<FeedBackSalesNotOnBehalfUserDataDto> {

  @Autowired
  private SalesNotOnBehalfFeedbackSendingEmailProcessor salesNotOnBehalfFeedbackSendingEmailProcessor;

  //@formatter:off
  @Override
  public FeedBackSalesNotOnBehalfUserDataDto getUserData(UserInfo user) {
    return FeedBackSalesNotOnBehalfUserDataDto.builder()
        .userId(user.getId())
        .userEmail(user.getEmail())
        .userPhone(eshopUserRepository.findPhoneById(user.getId()).orElse(StringUtils.EMPTY))
        .build();
  }
  //@formatter:on

  @Override
  protected FeedbackSendingEmailProcessor getSendingEmailProcessor() {
    return salesNotOnBehalfFeedbackSendingEmailProcessor;
  }
}
