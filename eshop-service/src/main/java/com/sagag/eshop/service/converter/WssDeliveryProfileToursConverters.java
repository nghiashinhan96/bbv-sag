package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.WssDeliveryProfileTours;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileToursDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class WssDeliveryProfileToursConverters {

  public static WssDeliveryProfileToursDto convertToDeliveryProfileDto(
      final WssDeliveryProfileTours deliveryProfileTour) {
    return WssDeliveryProfileToursDto.builder().id(deliveryProfileTour.getId())
        .isOverNight(deliveryProfileTour.isOverNight())
        .pickupWaitDuration(deliveryProfileTour.getPickupWaitDuration())
        .supplierTourDay(deliveryProfileTour.getSupplierTourDay())
        .supplierTourTime(DateUtils.toStringDate(deliveryProfileTour.getSupplierDepartureTime(),
            DateUtils.SHORT_TIME_PATTERN))
        .wssTour(WssTourConverters.convertToWssTourDto(deliveryProfileTour.getWssTour())).build();
  }

  public static List<WssDeliveryProfileToursDto> convertToDeliveryProfileDto(
      final List<WssDeliveryProfileTours> deliveryProfileTours) {
    if (CollectionUtils.isEmpty(deliveryProfileTours)) {
      return new ArrayList<>();
    }
    return deliveryProfileTours.stream().map(optionalDeliveryProfileConverter())
        .collect(Collectors.toList());
  }

  public static Function<WssDeliveryProfileTours, WssDeliveryProfileToursDto> optionalDeliveryProfileConverter() {
    return WssDeliveryProfileToursConverters::convertToDeliveryProfileDto;
  }
}
