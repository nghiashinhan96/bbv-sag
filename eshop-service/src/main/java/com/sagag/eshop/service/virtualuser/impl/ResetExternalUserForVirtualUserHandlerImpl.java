package com.sagag.eshop.service.virtualuser.impl;

import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.virtualuser.VirtualUserHandler;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ResetExternalUserForVirtualUserHandlerImpl implements VirtualUserHandler {

  @Autowired
  private ExternalUserService externalUserService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void accept(List<VVirtualUser> virtualUsers) {
    final List<Long> userIds = userIdListExtractor().apply(virtualUsers);
    log.info("Resting the pool of external user by virtual users = {}", userIds);
    if (CollectionUtils.isEmpty(userIds)) {
      return;
    }
    // Release DVSE relating to virtual users
    externalUserService.releaseVirtualUsers(userIds);
  }
}
