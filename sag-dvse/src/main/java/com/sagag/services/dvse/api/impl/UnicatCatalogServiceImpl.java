package com.sagag.services.dvse.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.dvse.api.UnicatArticleService;
import com.sagag.services.dvse.api.UnicatCartService;
import com.sagag.services.dvse.api.UnicatCatalogService;
import com.sagag.services.dvse.dto.unicat.OrderedItemDto;
import com.sagag.services.dvse.wsdl.unicat.GetArticleInformationResponse;
import com.sagag.services.dvse.wsdl.unicat.GetArticleInformations;
import com.sagag.services.dvse.wsdl.unicat.GetBackItems;
import com.sagag.services.dvse.wsdl.unicat.GetBackOrder;
import com.sagag.services.dvse.wsdl.unicat.SendOrder;
import com.sagag.services.dvse.wsdl.unicat.SendOrderResponse;
import com.sagag.services.dvse.wsdl.unicat.UnicatItem;
import com.sagag.services.hazelcast.api.UserCacheService;

import lombok.extern.slf4j.Slf4j;


/**
 * The service to wrap up the all specific logic and return to end point.
 */
@Service
@Slf4j
@CzProfile
public class UnicatCatalogServiceImpl implements UnicatCatalogService {

  @Autowired
  private UnicatArticleService unicatArticleService;

  @Autowired
  private UnicatCartService unicatCartService;

  @Autowired
  private UserCacheService userCacheService;

  @Override
  public GetArticleInformationResponse getArticleInfos(final GetArticleInformations request) {
    log.info("The XML request: \n{} ", XmlUtils.marshalWithPrettyMode(request));
    final UserInfo userInfo = authorizeUserBySid(request.getUser().getSid());

    List<UnicatItem> itemsInOrder = request.getItems().getItem();
    GetBackItems articleInformation = unicatArticleService.getArticleInformation(userInfo, itemsInOrder);

    GetArticleInformationResponse response = new GetArticleInformationResponse();
    response.setGetArticleInformationResult(articleInformation);
    return response;
  }

  @Override
  public SendOrderResponse addItemsToCart(SendOrder request) {
    log.info("The XML request: \n{} ", XmlUtils.marshalWithPrettyMode(request));
    final UserInfo userInfo = authorizeUserBySid(request.getSid());

    Map<String, Optional<Integer>> articleIdsAndQuantity = new HashMap<>();
    articleIdsAndQuantity.put(request.getArticleId(), Optional.of(request.getRequestedQuantity()));

    final OrderedItemDto addedItemsDto =
        unicatCartService.addItemsToCart(userInfo, articleIdsAndQuantity);

    final GetBackOrder backOrder = new GetBackOrder();
    backOrder.setItem(addedItemsDto.getOrder());
    backOrder.setOrderedItems(addedItemsDto.getAddedItems());

    final SendOrderResponse response = new SendOrderResponse();
    response.setSendOrderResult(backOrder);

    return response;
  }

  private UserInfo authorizeUserBySid(String sid) {
    UserInfo userInfo = userCacheService.getBySid(sid);
    if (Objects.isNull(userInfo)) {
      final String msg = String.format("The user info with sid = %s is not stored in cache, "
          + "please re-login with e-Connect application", sid);
      log.error(msg);
      throw new AccessDeniedException(msg);
    }
    LocaleContextHolder.setLocale(userInfo.getUserLocale());
    log.debug("Getting user from cache");
    return userInfo;
  }

}
