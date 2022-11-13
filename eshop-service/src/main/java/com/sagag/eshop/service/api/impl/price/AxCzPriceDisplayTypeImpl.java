package com.sagag.eshop.service.api.impl.price;

import com.sagag.eshop.service.api.PriceDisplayTypeService;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.profiles.AxCzProfile;

import org.springframework.stereotype.Component;

@Component
@AxCzProfile
public class AxCzPriceDisplayTypeImpl implements PriceDisplayTypeService {

  @Override
  public PriceDisplayTypeEnum getDefaultPriceDisplayType() {
    return PriceDisplayTypeEnum.DPC_GROSS;
  }

}
