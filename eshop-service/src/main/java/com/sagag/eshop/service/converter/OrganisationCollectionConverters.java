package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Function;

@UtilityClass
public class OrganisationCollectionConverters {

  public static Function<OrganisationCollection, OrganisationCollectionDto> toCustomerSettingBO() {
    return input -> OrganisationCollectionDto.builder()
        .collectionShortName(input.getShortname())
        .name(input.getName())
        .build();
  }

  public static Function<OrganisationCollection, OrganisationCollectionDto> toCollectionInfo(
      List<PermissionConfigurationDto> permissions) {
    return input -> OrganisationCollectionDto.builder()
        .collectionShortName(input.getShortname())
        .name(input.getName())
        .permissions(permissions)
        .build();
  }

}
