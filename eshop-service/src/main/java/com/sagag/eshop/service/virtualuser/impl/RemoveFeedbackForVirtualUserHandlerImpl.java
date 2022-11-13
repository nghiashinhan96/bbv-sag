package com.sagag.eshop.service.virtualuser.impl;

import com.sagag.eshop.repo.api.feedback.FeedbackRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.service.virtualuser.VirtualUserHandler;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(6)
@Slf4j
public class RemoveFeedbackForVirtualUserHandlerImpl implements VirtualUserHandler {

  @Autowired
  private FeedbackRepository feedbackRepo;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void accept(List<VVirtualUser> virtualUsers) {
    final List<Long> userIds = userIdListExtractor().apply(virtualUsers);
    log.info("Removing feedback for users ids: {}", userIds);
    if (CollectionUtils.isEmpty(userIds)) {
      return;
    }
    feedbackRepo.removeFeedbackByUserIds(userIds);
  }
}
