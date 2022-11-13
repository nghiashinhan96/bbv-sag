package com.sagag.eshop.service.virtualuser.impl;

import com.sagag.eshop.repo.api.order.OrderHistoryRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.service.virtualuser.VirtualUserHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(3)
@Slf4j
public class SwapOrderHistoryToOriginalUserForVirtualUserHandlerImpl implements VirtualUserHandler {

  @Autowired
  private OrderHistoryRepository orderHistoryRepo;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void accept(List<VVirtualUser> virtualUsers) {
    final Map<Long, Long> userIdOriginalUserIdMap = userIdAndOrgIdMapExtractor()
      .apply(virtualUsers);
    log.info("Swapping virtual user with original user in order history for user ids: {}",
      userIdOriginalUserIdMap);
    if (MapUtils.isEmpty(userIdOriginalUserIdMap)) {
      return;
    }

    final List<Long> virtualUserIds = new ArrayList<>(userIdOriginalUserIdMap.keySet());
    List<OrderHistory> orderHistories = orderHistoryRepo.findByUserIds(virtualUserIds);
    if (CollectionUtils.isEmpty(orderHistories)) {
      return;
    }
    orderHistories.stream().forEach(o -> o.setUserId(userIdOriginalUserIdMap.get(o.getUserId())));
    orderHistoryRepo.saveAll(orderHistories);
  }
}
