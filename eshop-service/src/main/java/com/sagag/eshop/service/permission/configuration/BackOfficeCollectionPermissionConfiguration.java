package com.sagag.eshop.service.permission.configuration;

import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopPermissionRepository;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopPermission;
import com.sagag.eshop.repo.entity.collection.CollectionPermission;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BackOfficeCollectionPermissionConfiguration
    implements BackOfficePermissionConfiguration<OrganisationCollectionDto> {

  @Autowired
  private EshopPermissionRepository eshopPermissionRepository;

  @Autowired
  private EshopGroupRepository groupRepository;

  @Autowired
  private PermissionService permService;

  @Override
  public List<PermissionConfigurationDto> getPermissions(OrganisationCollectionDto setting) {
    Assert.notNull(setting, "CollectionRequestBody cannot be null");
    Assert.hasText(setting.getAffiliateShortName(), "AffiliateShortName cannot be empty");
    return eshopPermissionRepository.findAll().stream()
        .map(toPermissionDto(setting.getAffiliateShortName())).collect(Collectors.toList());
  }

  public List<PermissionConfigurationDto> getPermissions(String affShortName,
      List<CollectionPermission> collectionPermissions, boolean isCustomerUser) {
    Assert.hasText(affShortName, "AffiliateShortName cannot be empty");
    return eshopPermissionRepository.findAll().stream()
        .map(toPermissionDto(affShortName, collectionPermissions, isCustomerUser)).collect(Collectors.toList());
  }

  private Function<EshopPermission, PermissionConfigurationDto> toPermissionDto(
      String affShortName) {
    return perm -> PermissionConfigurationDto.builder().permission(perm.getPermission())
        .editable(isPermBelongToAff(affShortName, perm.getPermission()))
        .langKey(perm.getPermissisonKey()).permissionId(perm.getId())
        .enable(isPermBelongToAff(affShortName, perm.getPermission())).build();
  }

  private Function<EshopPermission, PermissionConfigurationDto> toPermissionDto(String affShortName,
      List<CollectionPermission> collectionPermissions, boolean isCustomerUser) {
    return perm -> PermissionConfigurationDto.builder().permission(perm.getPermission())
        .editable(isCustomerUser ?
            isAllow(affShortName, perm, collectionPermissions) :
            isPermBelongToAff(affShortName, perm.getPermission()))
        .langKey(perm.getPermissisonKey()).permissionId(perm.getId())
        .enable(isAllow(affShortName, perm, collectionPermissions)).build();
  }

  private boolean isAllow(String affShortName, EshopPermission permission,
      List<CollectionPermission> collectionPermissions) {
    return isPermBelongToAff(affShortName, permission.getPermission())
        && collectionPermissions.stream().map(CollectionPermission::getEshopPermissionId)
            .anyMatch(id -> permission.getId() == id);
  }

  @Override
  public void updatePermisions(OrganisationCollectionDto setting) {
    boolean isNotAllow =
        setting.getPermissions().stream().filter(PermissionConfigurationDto::isEnable)
            .anyMatch(p -> !isPermBelongToAff(setting.getAffiliateShortName(), p.getPermission()));
    if (isNotAllow) {
      throw new IllegalArgumentException("Permission not allow");
    }
    updatePermissionForCustomer(setting.getCustomerSettingIds(), setting.getPermissions());
    updatePermissionForCustomer(setting.getFinalCustomerSettingIds(),
        setting.getPermissions()
            .stream()
            .filter(p -> isValidToAssignFinalCustomerPermission(p.getPermission()))
            .collect(Collectors.toList()));
  }

  private void updatePermissionForCustomer(List<Integer> customerSettingIds,
      List<PermissionConfigurationDto> permission) {
    List<EshopGroup> eshopGroup = findEshopGroup(customerSettingIds);
    if (CollectionUtils.isEmpty(eshopGroup)) {
      // No customer inside the collection so don't need to upgrade permission
      return;
    }
    permission.stream().filter(Objects::nonNull).forEach(updatePermissionForEshopGroups(eshopGroup));
  }

  private Consumer<PermissionConfigurationDto> updatePermissionForEshopGroups(List<EshopGroup> eshopGroups) {
    return configuration -> permService.updatePermission(eshopGroups,
        configuration.getPermissionId(), configuration.isEnable());
  }

  private List<EshopGroup> findEshopGroup(List<Integer> customerSettingIds) {
    if (CollectionUtils.isEmpty(customerSettingIds)) {
      // No customer inside the collection
      return new ArrayList<>();
    }
    return groupRepository.findByCustomerSettingIds(customerSettingIds);
  }
}
