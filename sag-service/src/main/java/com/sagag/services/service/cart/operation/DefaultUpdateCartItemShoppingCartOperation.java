package com.sagag.services.service.cart.operation;

import com.sagag.eshop.repo.hz.entity.*;
import com.sagag.eshop.service.dto.*;
import com.sagag.services.common.aspect.*;
import com.sagag.services.hazelcast.domain.cart.*;
import com.sagag.services.hazelcast.request.*;
import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class DefaultUpdateCartItemShoppingCartOperation
    extends AbstractShoppingCartOperation<UpdateAmountRequestBody, ShoppingCart> {

  @Autowired
  private DefaultUpdateInfoShoppingCartOperation updateInfoShoppingCartOperation;

  @Override
  @LogExecutionTime
  public ShoppingCart execute(UserInfo user, UpdateAmountRequestBody body, ShopType shopType,
      Object... additionals) {
    final String vehId = body.getVehicleId();
    final String idSagSys = body.getPimId();
    final int quantity = body.getAmount();

    final String cartKey = CartKeyGenerators.cartKey(user.key(), vehId, idSagSys, shopType);
    if (quantity == 0) {
      cartManager().remove(user.key(), cartKey);
      ShoppingCart shoppingCart = checkoutCart(user, shopType);
      shoppingCart.getItems()
      .removeIf(cartItem -> StringUtils.equals(cartItem.getCartKey(), cartKey));

      shoppingCart = reloadShoppingCart(user, shoppingCart);

      couponBusService.validateCoupon(user, shoppingCart);
      return shoppingCart;
    }
    cartManager().update(user.key(), cartKey, quantity);
    final ShoppingCart shoppingCart = checkoutCart(user, shopType);
    shoppingCart.getItems().stream()
    .filter(e -> StringUtils.equals(e.getCartKey(), cartKey))
    .forEach(e -> {
      e.setQuantity(quantity);
      e.getArticle().setAmountNumber(quantity);
      CollectionUtils.emptyIfNull(e.getAttachedArticles()).stream().filter(Objects::nonNull)
      .forEach(attached -> attached.setAmountNumber(quantity));
    });

    // find a way to only find price of updated article
    updateInfoShoppingCartOperation.execute(user, shoppingCart, shopType, vehId, idSagSys, true);
    couponBusService.validateCoupon(user, shoppingCart);

    return shoppingCart;
  }

}
