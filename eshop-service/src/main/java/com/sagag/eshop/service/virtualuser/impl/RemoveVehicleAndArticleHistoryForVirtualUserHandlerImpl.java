package com.sagag.eshop.service.virtualuser.impl;

import com.sagag.eshop.repo.api.UserArticleHistoryRepository;
import com.sagag.eshop.repo.api.UserVehicleHistoryRepository;
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
@Order(5)
@Slf4j
public class RemoveVehicleAndArticleHistoryForVirtualUserHandlerImpl implements VirtualUserHandler {

  @Autowired
  private UserVehicleHistoryRepository userVehicleHistoryRepo;

  @Autowired
  private UserArticleHistoryRepository userArticleHistoryRepo;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void accept(List<VVirtualUser> virtualUsers) {
    final List<Long> userIds = userIdListExtractor().apply(virtualUsers);
    log.info("Removing vehicle history for users ids: {}", userIds);
    if (CollectionUtils.isEmpty(userIds)) {
      return;
    }
    userVehicleHistoryRepo.removeVehicleHistoryByUserIds(userIds);
    userArticleHistoryRepo.removeArticleHistoryByUserIds(userIds);
  }
}
