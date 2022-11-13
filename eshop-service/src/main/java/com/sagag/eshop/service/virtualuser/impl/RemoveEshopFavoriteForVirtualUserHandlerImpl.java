package com.sagag.eshop.service.virtualuser.impl;

import com.sagag.eshop.repo.api.EshopFavoriteRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.service.virtualuser.VirtualUserHandler;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(7)
@Slf4j
public class RemoveEshopFavoriteForVirtualUserHandlerImpl implements VirtualUserHandler {

  @Autowired
  private EshopFavoriteRepository eshopFavoriteRepo;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void accept(List<VVirtualUser> virtualUsers) {
    final List<Long> userIds = userIdListExtractor().apply(virtualUsers);
    log.info("Removing favorite items for users ids: {}", userIds);
    if (CollectionUtils.isEmpty(userIds)) {
      return;
    }
    eshopFavoriteRepo.removeFavoriteItemsByUserIds(userIds);
  }

}
