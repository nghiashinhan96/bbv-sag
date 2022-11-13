package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultAddCartItemShoppingCartOperation
    extends AbstractAddCartItemShoppingCartOperation<List<ShoppingCartRequestBody>> {

  @Override
  @LogExecutionTime
  public ShoppingCart execute(UserInfo user, List<ShoppingCartRequestBody> cartRequests,
      ShopType shopType , Object... additionals) {
    if (CollectionUtils.isEmpty(cartRequests)) {
      throw new ValidationException("Cart items should not be empty");
    }
    log.debug("Start add new shopping cart item(s)");

    final Date addedTime = Calendar.getInstance().getTime();

    final List<CartItemDto> items = ListUtils.defaultIfNull(cartManager()
        .checkoutCart(user.getCachedUserId(), user.getCustNrStr(), shopType), new ArrayList<>());
    cartRequests.forEach(
        addCreateCartItemJobConsumer(user, addedTime, shopType, items));

    final ShoppingCart shoppingCart = reloadShoppingCart(user, doCheckoutCart(user, items));
    log.debug("End add new shopping cart item(s)");

    return shoppingCart;
  }

}
