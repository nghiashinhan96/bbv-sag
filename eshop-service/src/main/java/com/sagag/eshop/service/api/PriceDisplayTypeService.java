package com.sagag.eshop.service.api;

import com.sagag.services.common.enums.PriceDisplayTypeEnum;

public interface PriceDisplayTypeService {

  /**
   * Get default price display type
   *
   * @return default default price display type {@link PriceDisplayTypeEnum}
   */
  PriceDisplayTypeEnum getDefaultPriceDisplayType();

}
