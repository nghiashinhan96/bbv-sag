package com.sagag.services.service.cart.operation;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.api.CartManagerService;

public interface ShoppingCartOperation<T, R> {

  R execute(UserInfo user, T criteria, ShopType shopType, Object... additionals);

  CartManagerService cartManager();

}
