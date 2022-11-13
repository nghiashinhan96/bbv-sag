package com.sagag.services.hazelcast.user_context;

import com.sagag.eshop.repo.api.DeliveryTypesRepository;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.hazelcast.api.NextWorkingDateCacheService;
import com.sagag.services.hazelcast.domain.user.EshopContextType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public abstract class BasketContextMapper implements IContextMapper {


  @Autowired
  protected NextWorkingDateCacheService nextWorkingDateCacheService;

  @Autowired
  protected DeliveryTypesRepository deliveryRepo;

  protected boolean isChangedPickupBranch(final CustomerBranch currentCustBranch,
      final CustomerBranch newCustBranch) {
    return Objects.nonNull(currentCustBranch) && Objects.nonNull(newCustBranch) && !StringUtils
        .equalsIgnoreCase(currentCustBranch.getBranchId(), newCustBranch.getBranchId());
  }

  @Override
  public EshopContextType type() {
    return EshopContextType.BASKET_CONTEXT;
  }


}
