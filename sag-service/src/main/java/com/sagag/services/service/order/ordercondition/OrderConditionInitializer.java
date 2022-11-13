package com.sagag.services.service.order.ordercondition;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;

public interface OrderConditionInitializer {

  EshopBasketContext initialize(UserInfo user);

}
