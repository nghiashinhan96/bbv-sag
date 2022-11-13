package com.sagag.services.service.virtualuser.impl;

import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.service.virtualuser.VirtualUserHandler;
import com.sagag.services.hazelcast.api.CartManagerService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class ClearShoppingCartForVirtualUserHandlerImpl implements VirtualUserHandler {

  @Autowired
  private CartManagerService cartManagerService;

  @Override
  public void accept(List<VVirtualUser> virtualUsers) {
    final List<Long> userIds = userIdListExtractor().apply(virtualUsers);
    log.info("Removing shopping cart items for users ids: {}", userIds);
    if (CollectionUtils.isEmpty(userIds)) {
      return;
    }
    cartManagerService.clearCartByUserIds(userIds);
  }
}
