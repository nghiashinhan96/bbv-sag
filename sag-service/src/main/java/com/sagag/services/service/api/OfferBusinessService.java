package com.sagag.services.service.api;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.request.offer.OfferPositionItemRequestBody;
import com.sagag.services.service.request.offer.OfferRequestBody;

import java.util.List;

public interface OfferBusinessService {

  /**
   * Updates selected offer when each actions.
   *
   * @param user
   * @param body
   * @return the updated offer
   */
  OfferDto updateOffer(UserInfo user, OfferRequestBody body);

  /**
   * Adds all articles from offer to shopping basket
   *
   * @param user
   * @param offerPositionRequests
   * @return
   */
  ShoppingCart orderOffer(UserInfo user, List<OfferPositionItemRequestBody> offerPositionRequests,
      ShopType shopType);
}
