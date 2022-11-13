package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.order.VFinalCustomerOrder;
import com.sagag.eshop.service.dto.order.VFinalCustomerOrderDto;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

/**
 * Utility provide some converters of VFinalCustomerOrder.
 */
@UtilityClass
public final class VFinalCustomerOrderConverters {

  public static Function<VFinalCustomerOrder, VFinalCustomerOrderDto> converter() {
    return VFinalCustomerOrderDto::new;
  }
}
