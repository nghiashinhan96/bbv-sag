package com.sagag.services.service.cart.operation;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;

import org.springframework.stereotype.Component;

@Component
public class DefaultClearCartShoppingCartOperation
    extends AbstractShoppingCartOperation<Void, Void> {

  @Override
  public Void execute(UserInfo user, Void criteria, ShopType shopType, Object... additionals) {
    cartManager().clearCart(user.getCachedUserId(), user.getCustNrStr(), shopType);
    return null;
  }

}
