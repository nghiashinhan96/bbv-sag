package com.sagag.services.service.order.ordercondition;

import com.google.common.base.Objects;
import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.profiles.TopmotiveErpProfile;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.service.request.ViewOrderHistoryRequestBody;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;

@Component
@TopmotiveErpProfile
public class TmOrderConditionInitializer extends AbstractOrderConditionInitializer {

  @Override
  public EshopBasketContext initialize(UserInfo user) {
    final EshopBasketContext orderConditions = super.initialize(user);

    if (user.isFinalUserRole()) {
      return orderConditions;
    }
    List<DeliveryTypeDto> deliveryTypes =
        Optional.ofNullable(user).filter(userInfo -> userInfo.hasCust())
            .map(u -> u.getCustomer().getStakisDeliveryTypes()).orElse(null);
    if (CollectionUtils.isNotEmpty(deliveryTypes)) {
      orderConditions.setDeliveryType(deliveryTypes.stream()
          .filter(dt -> Objects.equal(dt.getDescCode(), user.getCustomer().getSendMethod()))
          .findFirst().orElse(null));
    }

    return orderConditions;
  }

  protected Predicate<Entry<String, VCustomerOrderHistory>> filterCondition(
      ViewOrderHistoryRequestBody condition) {
    return order -> StringUtils.isBlank(condition.getOrderNumber())
        || StringUtils.equals(order.getKey(), condition.getOrderNumber());
  }

}
