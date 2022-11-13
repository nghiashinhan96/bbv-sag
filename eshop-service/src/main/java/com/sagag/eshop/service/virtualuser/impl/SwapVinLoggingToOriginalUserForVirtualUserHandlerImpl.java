package com.sagag.eshop.service.virtualuser.impl;

import com.sagag.eshop.repo.api.VinLoggingRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.repo.entity.VinLogging;
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
@Order(4)
@Slf4j
public class SwapVinLoggingToOriginalUserForVirtualUserHandlerImpl implements VirtualUserHandler {

  @Autowired
  private VinLoggingRepository vinLoggingRepo;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void accept(List<VVirtualUser> virtualUsers) {
    final Map<Long, Long> userIdOriginalUserIdMap = userIdAndOrgIdMapExtractor()
      .apply(virtualUsers);
    log.info("Swap virtual user with original user in vin logging for user ids: {}",
      userIdOriginalUserIdMap);
    if (MapUtils.isEmpty(userIdOriginalUserIdMap)) {
      return;
    }
    final List<Long> virtualUserIds = new ArrayList<>(userIdOriginalUserIdMap.keySet());
    List<VinLogging> vinLoggingList = vinLoggingRepo.findByUserIds(virtualUserIds);
    if (CollectionUtils.isEmpty(vinLoggingList)) {
      return;
    }
    vinLoggingList.stream().forEach(o -> o.setUserId(userIdOriginalUserIdMap.get(o.getUserId())));
    vinLoggingRepo.saveAll(vinLoggingList);
  }
}
