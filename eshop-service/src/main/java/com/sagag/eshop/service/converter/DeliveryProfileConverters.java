package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.DeliveryProfile;
import com.sagag.eshop.service.dto.CsvDeliveryProfileDto;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileSavingDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@UtilityClass
public class DeliveryProfileConverters {

  public Function<CsvDeliveryProfileDto, DeliveryProfile> deliveryProfileConverter() {

    return col -> DeliveryProfile.builder().rawCountry(col.getCountry()).createdDate()
        .createdUserId(col.getCreatedUserId()).deliveryBranchId(col.getDeliveryBranchId())
        .deliveryProfileId(col.getDeliveryProfileId()).deliveryDuration(col.getDeliveryDuration())
        .distributionBranchId(col.getDistributionBranchId()).lastDelivery(col.getLastDelivery())
        .latestTime(col.getLatestTime()).modifiedUserId(col.getModifiedUserId())
        .deliveryProfileName(col.getDeliveryProfileName()).nextDay(col.getNextDay())
        .vendorCutOffTime(col.getVendorCutOffTime()).build();
  }

  public Function<DeliveryProfile, DeliveryProfileDto> deliveryProfileDtoConverter() {

    return col -> DeliveryProfileDto.builder().country(col.getCountry()).id(col.getId())
        .deliveryBranchId(col.getDeliveryBranchId()).deliveryProfileId(col.getDeliveryProfileId())
        .deliveryDuration(col.getDeliveryDuration())
        .distributionBranchId(col.getDistributionBranchId()).lastDelivery(col.getLastDelivery())
        .latestTime(col.getLatestTime()).deliveryProfileName(col.getDeliveryProfileName())
        .nextDay(col.getNextDay()).vendorCutOffTime(col.getVendorCutOffTime()).build();
  }

  public BiFunction<DeliveryProfileSavingDto, Optional<DeliveryProfile>, DeliveryProfile> deliveryProfileEntityConverter() {
    return (profile, entityOpt) -> {
      DateTimeFormatter timeOptionalPattern =
          DateTimeFormatter.ofPattern(DateUtils.TIME_OPTIONAL_PATTERN);
      final DeliveryProfile deliveryProfile =
          entityOpt.map(SerializationUtils::clone).orElseGet(DeliveryProfile::new);

      Optional.ofNullable(profile.getCountry()).filter(StringUtils::isNotBlank)
          .ifPresent(deliveryProfile::setCountry);

      Optional.ofNullable(profile.getDeliveryProfileId())
          .ifPresent(deliveryProfile::setDeliveryProfileId);

      Optional.ofNullable(profile.getDistributionBranchId())
          .ifPresent(deliveryProfile::setDistributionBranchId);

      Optional.ofNullable(profile.getDeliveryBranchId())
          .ifPresent(deliveryProfile::setDeliveryBranchId);

      deliveryProfile.setNextDay(profile.getNextDay());

      Optional.ofNullable(profile.getVendorCutOffTime())
          .ifPresent(vendorCutoffTime -> deliveryProfile
              .setVendorCutOffTime(LocalTime.parse(vendorCutoffTime, timeOptionalPattern)));

      Optional.ofNullable(profile.getLastDelivery()).ifPresent(lastDelivery -> deliveryProfile
          .setLastDelivery(LocalTime.parse(lastDelivery, timeOptionalPattern)));

      Optional.ofNullable(profile.getLatestTime()).ifPresent(lastestTime -> deliveryProfile
          .setLatestTime(LocalTime.parse(lastestTime, timeOptionalPattern)));

      Optional.ofNullable(profile.getDeliveryDuration())
          .ifPresent(deliveryProfile::setDeliveryDuration);

      Optional.ofNullable(profile.getDeliveryProfileName()).filter(StringUtils::isNotBlank)
          .ifPresent(deliveryProfile::setDeliveryProfileName);

      return deliveryProfile;
    };

  }

  public Function<DeliveryProfile, DeliveryProfileDto> deliveryProfileToProfileNameConverter() {
    return de -> DeliveryProfileDto.builder().deliveryProfileId(de.getDeliveryBranchId())
        .deliveryProfileName(de.getDeliveryProfileName()).build();
  }

}
