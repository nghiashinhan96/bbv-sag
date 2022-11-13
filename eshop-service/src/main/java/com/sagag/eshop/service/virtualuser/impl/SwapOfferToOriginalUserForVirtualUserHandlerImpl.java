package com.sagag.eshop.service.virtualuser.impl;

import com.sagag.eshop.repo.api.offer.OfferAddressRepository;
import com.sagag.eshop.repo.api.offer.OfferPersonRepository;
import com.sagag.eshop.repo.api.offer.OfferPositionRepository;
import com.sagag.eshop.repo.api.offer.OfferRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.repo.entity.offer.Offer;
import com.sagag.eshop.repo.entity.offer.OfferAddress;
import com.sagag.eshop.repo.entity.offer.OfferPerson;
import com.sagag.eshop.repo.entity.offer.OfferPosition;
import com.sagag.eshop.service.virtualuser.VirtualUserHandler;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(2)
@Slf4j
public class SwapOfferToOriginalUserForVirtualUserHandlerImpl implements VirtualUserHandler {

  @Autowired
  private OfferRepository offerRepo;

  @Autowired
  private OfferPersonRepository offerPersonRepo;

  @Autowired
  private OfferAddressRepository offerAddressRepo;

  @Autowired
  private OfferPositionRepository offerPositionRepo;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void accept(List<VVirtualUser> virtualUsers) {
    final Map<Long, Long> userIdOriginalUserIdMap = userIdAndOrgIdMapExtractor()
      .apply(virtualUsers);
    log.info("Swapping virtual user with original user in offer for user ids: {}",
      userIdOriginalUserIdMap);
    if (MapUtils.isEmpty(userIdOriginalUserIdMap)) {
      return;
    }

    updateOfferCreatedByVirtualUsers(userIdOriginalUserIdMap);
    updateOfferModifiedByVirtualUsers(userIdOriginalUserIdMap);

    updateOfferAddressesCreatedByVirtualUsers(userIdOriginalUserIdMap);
    updateOfferAddressesModifiedByVirtualUsers(userIdOriginalUserIdMap);

    updateOfferPersonsCreatedByVirtualUsers(userIdOriginalUserIdMap);
    updateOfferPersonsModifiedByVirtualUsers(userIdOriginalUserIdMap);

    updateOfferPositionsCreatedByVirtualUsers(userIdOriginalUserIdMap);
    updateOfferPositionsModifiedByVirtualUsers(userIdOriginalUserIdMap);
  }

  private void updateOfferCreatedByVirtualUsers(final Map<Long, Long> userIdOriginalUserIdMap) {
    final Set<Long> virtualUserIds = userIdOriginalUserIdMap.keySet();
    final List<Offer> offersCreatedByVirtualUsers = offerRepo.findByCreatedUserIds(virtualUserIds);
    if (CollectionUtils.isEmpty(offersCreatedByVirtualUsers)) {
      return;
    }
    offersCreatedByVirtualUsers.stream()
      .forEach(od -> od.setCreatedUserId(userIdOriginalUserIdMap.get(od.getCreatedUserId())));
    offerRepo.saveAll(offersCreatedByVirtualUsers);
  }

  private void updateOfferModifiedByVirtualUsers(final Map<Long, Long> userIdOriginalUserIdMap) {
    final Set<Long> virtualUserIds = userIdOriginalUserIdMap.keySet();
    final List<Offer> offersModifiedByVirtualUsers =
      offerRepo.findByModifiedUserIds(virtualUserIds);
    if (CollectionUtils.isEmpty(offersModifiedByVirtualUsers)) {
      return;
    }
    offersModifiedByVirtualUsers.stream()
      .forEach(od -> od.setModifiedUserId(userIdOriginalUserIdMap.get(od.getModifiedUserId())));
    offerRepo.saveAll(offersModifiedByVirtualUsers);
  }

  private void updateOfferAddressesCreatedByVirtualUsers(
    final Map<Long, Long> userIdOriginalUserIdMap) {
    final Set<Long> virtualUserIds = userIdOriginalUserIdMap.keySet();
    final List<OfferAddress> offerAddressesCreatedByVirtualUsers =
      offerAddressRepo.findByCreatedUserIds(virtualUserIds);
    if (CollectionUtils.isEmpty(offerAddressesCreatedByVirtualUsers)) {
      return;
    }
    offerAddressesCreatedByVirtualUsers.stream()
      .forEach(od -> od.setCreatedUserId(userIdOriginalUserIdMap.get(od.getCreatedUserId())));
    offerAddressRepo.saveAll(offerAddressesCreatedByVirtualUsers);
  }

  private void updateOfferAddressesModifiedByVirtualUsers(
    final Map<Long, Long> userIdOriginalUserIdMap) {
    final Set<Long> virtualUserIds = userIdOriginalUserIdMap.keySet();
    final List<OfferAddress> offerAddressesModifiedByVirtualUsers =
      offerAddressRepo.findByModifiedUserIds(virtualUserIds);
    if (CollectionUtils.isEmpty(offerAddressesModifiedByVirtualUsers)) {
      return;
    }
    offerAddressesModifiedByVirtualUsers.stream()
      .forEach(od -> od.setModifiedUserId(userIdOriginalUserIdMap.get(od.getModifiedUserId())));
    offerAddressRepo.saveAll(offerAddressesModifiedByVirtualUsers);
  }

  private void updateOfferPersonsCreatedByVirtualUsers(
    final Map<Long, Long> virtualOriginalUserIdMap) {
    final Set<Long> virtualUserIds = virtualOriginalUserIdMap.keySet();
    final List<OfferPerson> offerPersonsCreatedByVirtualUsers =
      offerPersonRepo.findByCreatedUserIds(virtualUserIds);
    if (CollectionUtils.isEmpty(offerPersonsCreatedByVirtualUsers)) {
      return;
    }
    offerPersonsCreatedByVirtualUsers.stream()
      .forEach(od -> od.setCreatedUserId(virtualOriginalUserIdMap.get(od.getCreatedUserId())));
    offerPersonRepo.saveAll(offerPersonsCreatedByVirtualUsers);
  }

  private void updateOfferPersonsModifiedByVirtualUsers(
    final Map<Long, Long> userIdOriginalUserIdMap) {
    final Set<Long> virtualUserIds = userIdOriginalUserIdMap.keySet();
    final List<OfferPerson> offerPersonModifiedByVirtualUsers =
      offerPersonRepo.findByModifiedUserIds(virtualUserIds);
    if (CollectionUtils.isEmpty(offerPersonModifiedByVirtualUsers)) {
      return;
    }
    offerPersonModifiedByVirtualUsers.stream()
      .forEach(od -> od.setModifiedUserId(userIdOriginalUserIdMap.get(od.getModifiedUserId())));
    offerPersonRepo.saveAll(offerPersonModifiedByVirtualUsers);
  }

  private void updateOfferPositionsCreatedByVirtualUsers(
    final Map<Long, Long> userIdOriginalUserIdMap) {
    final Set<Long> virtualUserIds = userIdOriginalUserIdMap.keySet();
    final List<OfferPosition> offerPositionsCreatedByVirtualUsers =
      offerPositionRepo.findByCreatedUserIds(virtualUserIds);
    if (CollectionUtils.isEmpty(offerPositionsCreatedByVirtualUsers)) {
      return;
    }
    offerPositionsCreatedByVirtualUsers.stream()
      .forEach(od -> od.setCreatedUserId(userIdOriginalUserIdMap.get(od.getCreatedUserId())));
    offerPositionRepo.saveAll(offerPositionsCreatedByVirtualUsers);
  }

  private void updateOfferPositionsModifiedByVirtualUsers(
    final Map<Long, Long> userIdOriginalUserIdMap) {
    final Set<Long> virtualUserIds = userIdOriginalUserIdMap.keySet();
    final List<OfferPosition> offerPositionModifiedByVirtualUsers =
      offerPositionRepo.findByModifiedUserIds(virtualUserIds);
    if (CollectionUtils.isEmpty(offerPositionModifiedByVirtualUsers)) {
      return;
    }
    offerPositionModifiedByVirtualUsers.stream()
      .forEach(od -> od.setModifiedUserId(userIdOriginalUserIdMap.get(od.getModifiedUserId())));
    offerPositionRepo.saveAll(offerPositionModifiedByVirtualUsers);
  }
}
