package com.sagag.eshop.service.utils;

import com.sagag.eshop.repo.entity.collection.CollectionPermission;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@UtilityClass
public class OrganisationCollectionUtils {

  public String buildCollectionShortName(final String affiliateShortName) {
    return affiliateShortName + SagConstants.HYPHEN + RandomStringUtils.randomAlphanumeric(10);
  }

  public static Supplier<OrganisationCollectionsSettings> simpleSetting(OrganisationCollection collection,
      String settingKey) {
    return () -> OrganisationCollectionsSettings.builder()
        .collectionId(collection.getId())
        .settingKey(settingKey)
        .build();
  }

  public void validateCollectionRequest(OrganisationCollectionDto request) {
    Assert.notNull(request, "request cannot be null");
    Assert.hasText(request.getAffiliateShortName(), "affiliate cannot be empty");
    Assert.hasText(request.getName(), "collection name cannot be empty");
    Assert.notNull(request.getPermissions(), "permission list cannot be null");
  }

  public Function<OrganisationCollectionsSettings, OrganisationCollectionsSettings> toCollectionSettings(
      Map<String, String> settings, OrganisationCollection collection) {
    return orgCollSettings -> {
      String value = settings.getOrDefault(orgCollSettings.getSettingKey(), StringUtils.EMPTY);
      return OrganisationCollectionsSettings.builder()
          .collectionId(collection.getId())
          .settingKey(orgCollSettings.getSettingKey())
          .settingValue(StringUtils.defaultIfBlank(value, orgCollSettings.getSettingValue()))
          .build();
    };
  }

  public boolean isPermissionChanged(List<CollectionPermission> existedPermission,
      List<PermissionConfigurationDto> requestPermission) {
    List<Integer> existedPermissionId =
        existedPermission.stream().map(CollectionPermission::getEshopPermissionId).collect(Collectors.toList());
    List<Integer> requestPermissionId = requestPermission.stream().filter(PermissionConfigurationDto::isEnable)
        .map(PermissionConfigurationDto::getPermissionId).collect(Collectors.toList());
    return !CollectionUtils.isEqualCollection(existedPermissionId, requestPermissionId);
  }

}
