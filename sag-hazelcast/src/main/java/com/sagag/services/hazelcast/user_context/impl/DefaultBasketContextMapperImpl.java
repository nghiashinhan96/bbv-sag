package com.sagag.services.hazelcast.user_context.impl;

import com.sagag.eshop.repo.api.BranchRepository;
import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.repo.entity.DeliveryType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.ContextDto;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.hazelcast.domain.user.EshopContextType;
import com.sagag.services.hazelcast.user_context.BasketContextMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DefaultBasketContextMapperImpl extends BasketContextMapper {

  @Autowired
  private BranchRepository branchRepo;

  @Override
  public void map(UserInfo user, EshopContext context, ContextDto contextDto) {
    // update latest next working date to cache if user change pickup branch in order
    // condition section
    if (context.hasPickupBranchId()
        && isChangedPickupBranch(contextDto.getEshopBasketDto().getPickupBranch(),
            context.getEshopBasketContext().getPickupBranch())) {
      final String pickupBranchId = contextDto.getEshopBasketDto().getPickupBranch().getBranchId();
      nextWorkingDateCacheService.update(user, pickupBranchId);
      final Boolean isShowKSLMode = branchRepo.findOneByBranchNr(Integer.valueOf(pickupBranchId))
          .map(Branch::getValidForKSL).orElse(false);
      contextDto.getEshopBasketDto().setShowKSLMode(isShowKSLMode);
    }
    // If selection is counter transfer basket, set delivery type to PICKUP and order type to
    // counter transfer basket
    if (contextDto.isCounterBasketMode()) {
      final Optional<DeliveryType> pickupDelivery =
          deliveryRepo.findOneByDescCode(SendMethodType.PICKUP.name());
      pickupDelivery.ifPresent(item -> contextDto.getEshopBasketDto()
          .setDeliveryType(SagBeanUtils.map(item, DeliveryTypeDto.class)));
    }
    context.updateBasketContext(contextDto.getEshopBasketDto());
    log.debug("EshopContext: EshopBasketDto {}",
        SagJSONUtil.convertObjectToPrettyJson(contextDto.getEshopBasketDto()));
  }

  @Override
  public EshopContextType type() {
    return EshopContextType.BASKET_CONTEXT;
  }

}
