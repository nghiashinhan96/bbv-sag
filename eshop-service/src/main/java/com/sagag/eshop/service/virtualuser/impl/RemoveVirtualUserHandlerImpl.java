package com.sagag.eshop.service.virtualuser.impl;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.GroupUserRepository;
import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
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
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Slf4j
public class RemoveVirtualUserHandlerImpl implements VirtualUserHandler {

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Autowired
  private UserSettingsRepository userSettingsRepo;

  @Autowired
  private GroupUserRepository groupUserRepo;

  @Autowired
  private LoginRepository loginRepo;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void accept(List<VVirtualUser> virtualUsers) {
    final List<Long> userIds = userIdListExtractor().apply(virtualUsers);
    final List<Integer> settingsIds = userSettingIdListExtractor().apply(virtualUsers);
    log.info("Removing virtual user data for user ids: {} - setting ids = {}",
      userIds, settingsIds);
    if (!CollectionUtils.isEmpty(userIds)) {
      loginRepo.removeLoginByUserIds(userIds);
      groupUserRepo.removeGroupUserByUserIds(userIds);
      eshopUserRepo.removeEshopUsersByIds(userIds);
    }
    if (!CollectionUtils.isEmpty(settingsIds)) {
      userSettingsRepo.removeSettingsByIds(settingsIds);
    }
  }
}
