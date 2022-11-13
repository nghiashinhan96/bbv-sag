package com.sagag.eshop.service.converter;

import com.google.common.primitives.Longs;
import com.sagag.eshop.repo.entity.ExternalVendor;
import com.sagag.eshop.service.dto.CsvExternalVendorDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

@UtilityClass
public class ExternalConverters {

  public static Function<CsvExternalVendorDto, ExternalVendor> externalConverter() {
    return col -> ExternalVendor.builder().country(col.getCountry())
        .sagArticleGroup(col.getSagArticleGroup()).brandId(col.getBrandId())
        .vendorId(col.getVendorId()).vendorName(col.getVendorName())
        .vendorPriority(col.getVendorPriority()).deliveryProfileId(col.getDeliveryProfileId())
        .availabilityTypeId(col.getAvailabilityTypeId()).modifiedUserId(col.getModifiedUserId())
        .createdDate().createdUserId(col.getCreatedUserId()).build();
  }

  public static ExternalVendor dtoConvertToExternalVendor(ExternalVendorDto externalVendorDto) {
    return ExternalVendor.builder().country(externalVendorDto.getCountry())
        .id(externalVendorDto.getId()).sagArticleGroup(externalVendorDto.getSagArticleGroup())
        .brandId(externalVendorDto.getBrandId())
        .vendorId(externalVendorDto.getVendorId() == null ? null
            : Longs.tryParse(externalVendorDto.getVendorId()))
        .vendorName(StringUtils.isNoneBlank(externalVendorDto.getVendorName())
            ? externalVendorDto.getVendorName()
            : null)
        .vendorPriority(externalVendorDto.getVendorPriority())
        .deliveryProfileId(externalVendorDto.getDeliveryProfileId())
        .availabilityTypeId(externalVendorDto.getAvailabilityTypeId()).build();
  }
}
