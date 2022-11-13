package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.WssDeliveryProfile;
import com.sagag.eshop.repo.entity.WssDeliveryProfileTours;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileRequestDto;

import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@UtilityClass
public class WssDeliveryProfileConverters {

  public BiFunction<WssDeliveryProfileRequestDto, Optional<WssDeliveryProfile>,
  WssDeliveryProfile> wssDeliveryProfileEntityConverter() {
    return (profile, entityOpt) -> WssDeliveryProfile.builder().name(profile.getName())
        .description(profile.getDescription()).build();
  }

  public static WssDeliveryProfileTours wssDeliveryProfileToursEntityConverter(
      final WssDeliveryProfileRequestDto request) {
    return WssDeliveryProfileTours.builder().isOverNight(request.getIsOverNight())
        .pickupWaitDuration(request.getPickupWaitDuration()).supplierDepartureTime(DateUtils
            .convertStringToTime(request.getSupplierDepartureTime(), DateUtils.TIME_PATTERN))
        .supplierTourDay(request.getSupplierTourDay()).build();
  }

  public static Function<WssDeliveryProfileRequestDto,
    WssDeliveryProfileTours> wssDeliveryProfileToursEntityConverter() {
    return request -> WssDeliveryProfileTours.builder().isOverNight(request.getIsOverNight())
        .pickupWaitDuration(request.getPickupWaitDuration()).supplierDepartureTime(DateUtils
            .convertStringToTime(request.getSupplierDepartureTime(), DateUtils.TIME_PATTERN))
        .supplierTourDay(request.getSupplierTourDay()).build();
  }

  public static WssDeliveryProfileDto convertToDeliveryProfileDto(
      final WssDeliveryProfile deliveryProfile) {
    return WssDeliveryProfileDto.builder().id(deliveryProfile.getId())
        .orgId(deliveryProfile.getOrgId()).name(deliveryProfile.getName())
        .description(deliveryProfile.getDescription())
        .wssBranch(WssBranchConverters.convertToBranchDto(deliveryProfile.getWssBranch()))
        .wssDeliveryProfileToursDtos(WssDeliveryProfileToursConverters
            .convertToDeliveryProfileDto(deliveryProfile.getWssDeliveryProfileTours()))
        .build();
  }

  public static Function<WssDeliveryProfile,
    WssDeliveryProfileDto> optionalDeliveryProfileConverter() {
    return WssDeliveryProfileConverters::convertToDeliveryProfileDto;
  }
}
