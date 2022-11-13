package com.sagag.services.service.order.ordercondition;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.eshop.dto.CourierDto;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.service.order.location.CourierBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@SbProfile
public class SbOrderConditionInitializer extends AbstractOrderConditionInitializer {

  @Autowired
  private CourierBuilder courierBuilder;

  @Override
  public EshopBasketContext initialize(UserInfo user) {
    EshopBasketContext initializeOrderConditions = super.initialize(user);
    List<CourierDto> couriers = user.isSalesNotOnBehalf() ? Collections.emptyList()
        : courierBuilder.buildCourierServices(user.getCompanyName());
    initializeOrderConditions.setCourierService(CollectionUtils.emptyIfNull(couriers).stream()
        .filter(Objects::nonNull).findFirst().orElse(new CourierDto()));
    return initializeOrderConditions;
  }

}
