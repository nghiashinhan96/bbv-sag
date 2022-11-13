package com.sagag.services.ivds.utils;

import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@UtilityClass
public class EshopBasketContextUtils {

  public static String findSendMethod(final EshopBasketContext basketContext,
      final String defaultSendMethod) {
    if (Objects.isNull(basketContext)) {
      return defaultSendMethod;
    }
    final DeliveryTypeDto deliveryType = basketContext.getDeliveryType();
    if (Objects.isNull(deliveryType) || StringUtils.isBlank(deliveryType.getDescCode())) {
      return defaultSendMethod;
    }
    return basketContext.getDeliveryType().getDescCode();
  }

  public static String findDeliveryAddressId(final EshopBasketContext basketContext,
      final String defaultAddressId) {
    if (Objects.isNull(basketContext)) {
      return defaultAddressId;
    }
    return basketContext.getOrDefaultDeliveryAddressId(defaultAddressId);
  }
}
