package com.sagag.services.service.cart.operation;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DefaultRemoveCartKeysShoppingCartOperation
    extends AbstractShoppingCartOperation<String[], ShoppingCart> {

  @Autowired
  private DefaultUpdateInfoShoppingCartOperation updateInfoShoppingCartOperation;

  @Override
  public ShoppingCart execute(UserInfo user, String[] cartKeys, ShopType shopType,
      Object... additionals) {
    final boolean isAvailabilityReq = BooleanUtils.isTrue((Boolean) additionals[0]);
    log.debug("Start remove shopping cart item(s)");
    Assert.notEmpty(cartKeys, "The given cart keys must not be empty");

    // Get all cart keys to delete with the unique list of items
    final Set<String> cartKeySet = Arrays.stream(cartKeys).collect(Collectors.toSet());
    cartKeySet.forEach(cartKey -> cartManager().remove(user.key(), cartKey));
    final ShoppingCart shoppingCart = checkoutCart(user, shopType);

    shoppingCart.getItems().removeIf(cartItem -> cartKeySet.contains(cartItem.getCartKey()));

    updateInfoShoppingCartOperation.execute(user, shoppingCart, shopType, StringUtils.EMPTY,
        StringUtils.EMPTY, isAvailabilityReq);
    couponBusService.validateCoupon(user, shoppingCart);
    log.debug("End remove shopping cart item(s)");

    return shoppingCart;
  }

}
