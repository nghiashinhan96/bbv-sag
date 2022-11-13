package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.eshop.service.dto.offer.OfferPositionDto;
import com.sagag.services.common.enums.offer.OfferPositionType;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.api.DmsExportCacheService;
import com.sagag.services.hazelcast.api.HaynesProCacheService;
import com.sagag.services.hazelcast.api.VinOrderCacheService;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheJobDto;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.api.DmsService;
import com.sagag.services.service.api.OfferBusinessService;
import com.sagag.services.service.request.dms.DmsExportRequest;
import com.sagag.services.service.request.offer.OfferPositionItemRequestBody;
import com.sagag.services.service.utils.DmsRequestBuilder;
import com.sagag.services.service.utils.HaynesProLabourTimesUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DmsServiceImpl implements DmsService {

  @Autowired
  private DmsExportCacheService dmsExportCacheService;

  @Autowired
  private CartBusinessService cartBusinessService;

  @Autowired
  private CouponCacheService couponCacheService;

  @Autowired
  private VinOrderCacheService vinOrderCacheService;

  @Autowired
  private OfferBusinessService offerBusinessService;

  @Autowired
  private HaynesProCacheService haynesProCacheService;

  @Override
  public void export(final UserInfo user, final DmsExportRequest data) {
    log.debug("Exporting Offer by data = {}", data);
    final String key = user.key();
    dmsExportCacheService.add(new DmsRequestBuilder().exportRequest(data).buildContent(), key);
    cartBusinessService.clear(user, ShopType.DEFAULT_SHOPPING_CART);
    couponCacheService.clearCache(key);
    vinOrderCacheService.clearSearchCount(key);
    haynesProCacheService.clearLabourTimes(key);
    log.debug("Done export file!");
  }

  @Override
  public String download(final Long userId, final String customerNr) {
    log.debug("Downloading DMS content by userId = {} and customerNr = {}", userId, customerNr);
    final String key = UserInfo.userKey(userId.toString(), customerNr);
    String htmlContent = dmsExportCacheService.getFileContent(key);
    dmsExportCacheService.clearCache(key);
    return StringUtils.defaultIfBlank(htmlContent, StringUtils.EMPTY);
  }

  @Override
  @Transactional
  public void addOfferToShoppingCart(final UserInfo user, final OfferDto offer,
      final ShopType shopType) {
    log.debug("Adding offer to shopping cart by offer = {}", offer);
    final List<OfferPositionDto> offerPositions = offer.getOfferPositions();
    if (CollectionUtils.isEmpty(offerPositions)) {
      return;
    }

    // Add articles in context and not in context to the shopping basket
    final List<OfferPositionItemRequestBody> items = offerPositions.stream()
        .filter(offerPos -> OfferPositionType.VENDOR_ARTICLE.name().equals(offerPos.getType())
            || OfferPositionType.VENDOR_ARTICLE_WITHOUT_VEHICLE.name().equals(offerPos.getType()))
        .map(OfferPositionItemRequestBody::new).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(items)) {
      return;
    }

    // Add items in offer to the shopping basket
    offerBusinessService.orderOffer(user, items, shopType);

    addLabourTimes(user, offerPositions);
  }

  private void addLabourTimes(final UserInfo user, final List<OfferPositionDto> offerPositions) {
    // Add labour times to the haynespro cache
    final List<OfferPositionDto> labourTimes = offerPositions.stream()
        .filter(offerPos -> OfferPositionType.valueOf(offerPos.getType()).isHaynesProWork())
        .collect(Collectors.toList());

    final Map<String, List<OfferPositionDto>> labourTimeOfferPositions = new HashMap<>();
    labourTimes.forEach(pos -> labourTimeOfferPositions.compute(pos.getConnectVehicleId(),
        HaynesProLabourTimesUtils.reMapper(pos)));

    labourTimeOfferPositions.forEach((vehId, offPositions) -> {
      final List<HaynesProCacheJobDto> jobs = offPositions.stream()
          .map(HaynesProLabourTimesUtils.hpCacheJobConverter()).collect(Collectors.toList());
      haynesProCacheService.addLabourTimes(user.key(), vehId, jobs);
    });
  }

}
