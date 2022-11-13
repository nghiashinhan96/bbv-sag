package com.sagag.eshop.service.permission.configuration;

import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopPermissionRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.collection.CollectionPermissionRepository;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopPermission;
import com.sagag.eshop.repo.entity.collection.CollectionPermission;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.services.domain.bo.dto.CustomerSettingsBODto;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BackOfficeCustomerPermissionConfiguration
    implements BackOfficePermissionConfiguration<CustomerSettingsBODto> {

  @Autowired
  private EshopGroupRepository groupRepository;

  @Autowired
  private PermissionService permService;

  @Autowired
  private EshopPermissionRepository eshopPermissionRepository;

  @Autowired
  private CollectionPermissionRepository collectionPermissionRepository;

  @Autowired
  private OrganisationCollectionService organisationCollectionService;

  @Autowired
  private OrganisationService organisationService;

  @Autowired
  private OrganisationRepository organisationRepo;

  @Override
  @Transactional
  public List<PermissionConfigurationDto> getPermissions(CustomerSettingsBODto setting) {
    Assert.notNull(setting, "CustomerSettings cannot be null");
    List<EshopGroup> eshopGroups = groupRepository.findByCustomerSettingId(setting.getId());
    List<EshopPermission> perms = eshopPermissionRepository.findAll();
    if (CollectionUtils.isEmpty(perms) || CollectionUtils.isEmpty(eshopGroups)) {
      return new ArrayList<>();
    }
    OrganisationCollection collection =
        organisationCollectionService.getCollectionByOrgId(setting.getOrgId())
            .orElseThrow(() -> new IllegalArgumentException("Collection not found"));
    List<CollectionPermission> maxPermissionAllow =
        collectionPermissionRepository.findByCollectionShortName(collection.getShortname());
    return perms.stream().map(toPermissionConfigurationDto(maxPermissionAllow, eshopGroups))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void updatePermisions(CustomerSettingsBODto customerSettings) {
    List<EshopGroup> eshopGroups =
        groupRepository.findByCustomerSettingId(customerSettings.getId());
    if (CollectionUtils.isEmpty(customerSettings.getPerms())
        || CollectionUtils.isEmpty(eshopGroups)) {
      return;
    }
    customerSettings.getPerms().forEach(s -> updatePermission(eshopGroups, s));

    Integer orgId = customerSettings.getOrgId();
    if (organisationService.isWholesalerCustomer(orgId)) {
      List<Integer> finalCustomerIds = organisationRepo.findIdsByParentId(orgId);
      updatePermissionForFinalCustomer(customerSettings, finalCustomerIds);
    }
  }

  private void updatePermissionForFinalCustomer(CustomerSettingsBODto customerSettings,
      List<Integer> finalCustomerIds) {
    if (CollectionUtils.isEmpty(finalCustomerIds)) {
      return;
    }
    groupRepository.findByOrgIds(finalCustomerIds).ifPresent(groups -> {
      List<PermissionConfigurationDto> perms = customerSettings.getPerms().stream()
          .filter(p -> isValidToAssignFinalCustomerPermission(p.getPermission()))
          .collect(Collectors.toList());
      perms.forEach(s -> updatePermission(groups, s));
    });
  }

  private Function<EshopPermission, PermissionConfigurationDto> toPermissionConfigurationDto(
      List<CollectionPermission> collectionPermissions, List<EshopGroup> eshopGroups) {
    return eshopPermission -> {
      boolean isEditable =
          collectionPermissions.stream().map(CollectionPermission::getEshopPermissionId)
              .anyMatch(id -> id == eshopPermission.getId());
      return PermissionConfigurationDto.builder().permission(eshopPermission.getPermission())
          .editable(isEditable).langKey(eshopPermission.getPermissisonKey())
          .permissionId(eshopPermission.getId())
          .enable(permService.hasPermission(eshopGroups, eshopPermission.getId())).build();
    };
  }

  private void updatePermission(List<EshopGroup> eshopGroups,
      PermissionConfigurationDto configuration) {
    Optional.ofNullable(configuration).ifPresent(c -> permService.updatePermission(eshopGroups,
        c.getPermissionId(), configuration.isEnable()));
  }
}
