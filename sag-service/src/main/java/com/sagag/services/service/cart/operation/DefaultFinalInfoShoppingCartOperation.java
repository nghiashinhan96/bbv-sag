package com.sagag.services.service.cart.operation;

import com.sagag.eshop.repo.hz.entity.*;
import com.sagag.eshop.service.dto.*;
import com.sagag.services.hazelcast.domain.cart.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class DefaultFinalInfoShoppingCartOperation
    extends AbstractShoppingCartOperation<Void, ShoppingCart> {

  @Autowired
  private DefaultUpdateInfoShoppingCartOperation updateInfoShoppingCartOperation;

  @Override
  public ShoppingCart execute(UserInfo user, Void criteria, ShopType shopType,
    Object... additionals) {
    final boolean isAvailabilityReq = (boolean) additionals[0];
    final ShoppingCart cart = checkoutCart(user, shopType);

    updateInfoShoppingCartOperation.execute(user, cart, shopType, StringUtils.EMPTY,
        StringUtils.EMPTY, isAvailabilityReq);

    couponBusService.validateCoupon(user, cart);

    return cart;
  }

}
