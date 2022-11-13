package com.sagag.services.hazelcast.user_context.impl;

import com.sagag.eshop.repo.entity.DeliveryType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.ContextDto;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.hazelcast.domain.user.EshopContextType;
import com.sagag.services.hazelcast.user_context.BasketContextMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SbProfile
@Service
public class SbBasketContextMapperImpl extends BasketContextMapper {

  @Override
  public void map(UserInfo user, EshopContext context, ContextDto contextDto) {
    if (contextDto.isCounterBasketMode()) {
      final Optional<DeliveryType> pickupDelivery =
          deliveryRepo.findOneByDescCode(SendMethodType.PICKUP.name());
      pickupDelivery.ifPresent(item -> contextDto.getEshopBasketDto()
          .setDeliveryType(SagBeanUtils.map(item, DeliveryTypeDto.class)));
    }
    context.updateBasketContext(contextDto.getEshopBasketDto());
    List<EshopBasketContext> updateEshopBasketContext = new ArrayList<>();
    Optional.ofNullable(contextDto.getEshopBasketDto().getEshopBasketContextByLocation())
        .ifPresent(item -> item.forEach(eshop -> {
          EshopContext eshopContext = new EshopContext();
          eshopContext.updateBasketContext(eshop);
          updateEshopBasketContext.add(eshopContext.getEshopBasketContext());

        }));
    context.getEshopBasketContext().setEshopBasketContextByLocation(updateEshopBasketContext);
    log.debug("EshopContext: EshopBasketDto {}",
        SagJSONUtil.convertObjectToPrettyJson(contextDto.getEshopBasketDto()));
  }

  @Override
  public EshopContextType type() {
    return EshopContextType.BASKET_CONTEXT;
  }

}
