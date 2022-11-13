package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopPermissionRepository;
import com.sagag.eshop.repo.api.GroupPermissionRepository;
import com.sagag.eshop.repo.entity.EshopFunction;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopPermission;
import com.sagag.eshop.repo.entity.GroupPermission;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.dto.FunctionDto;
import com.sagag.eshop.service.dto.PermissionDto;
import com.sagag.eshop.service.permission.configuration.PermissionConfiguration;
import com.sagag.eshop.service.utils.PermissionUtils;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Permission implementation class.
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService, PermissionConfiguration {

  private static final String PERMISSION_NOT_EXIST = "Permission does not exist.";

  @Autowired
  private EshopPermissionRepository permissionRepo;

  @Autowired
  private GroupPermissionRepository groupPermissionRepo;

  @Autowired
  private EshopGroupRepository eshopGroupRepository;

  @Override
  public List<PermissionDto> getUserPermissions(long userId) {
    final List<Integer> defaultPerms = permissionRepo.findAllUserDefaultPermissions(userId);
    final List<GroupPermission> customizedPerms =
        groupPermissionRepo.findAllUserCustomizedPermissions(userId);
    return joinPermissions(defaultPerms, customizedPerms).stream()
        .map(PermissionServiceImpl::createPermissionDto).collect(Collectors.toList());
  }

  @Override
  public List<PermissionDto> getUserPermissionsByGroupId(int groupId) {
    final List<Integer> defaultPerms =
        permissionRepo.findAllUserDefaultPermissionsByGroupId(groupId);
    final List<GroupPermission> customizedPerms =
        groupPermissionRepo.findAllUserCustomizedPermissionsByGroupId(groupId);
    return joinPermissions(defaultPerms, customizedPerms).stream()
        .map(PermissionServiceImpl::createPermissionDto).collect(Collectors.toList());
  }

  @Override
  public boolean hasUrlPermission(List<FunctionDto> functions, String url) {
    return PermissionUtils.hasUrlPermission(functions, url);
  }

  @Override
  public boolean hasPermission(List<EshopGroup> eshopGroups, Integer permissionId) {
    for (EshopGroup eshopGroup : eshopGroups) {
      if (!hasPermission(eshopGroup, permissionId)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean hasPermission(Integer orgId, Integer permissionId) {
    List<EshopGroup> groups = eshopGroupRepository.findByOrgId(orgId);
    return CollectionUtils.emptyIfNull(groups).stream()
        .allMatch(gr -> hasPermission(gr, permissionId));
  }

  @Override
  public void updatePermission(List<EshopGroup> eshopGroups, Integer permissionId,
      boolean isEnable) {
    if (CollectionUtils.isEmpty(eshopGroups) || Objects.isNull(permissionId)) {
      return;
    }

    List<Integer> groupIds =
        eshopGroups.stream().map(EshopGroup::getId).collect(Collectors.toList());
    List<GroupPermission> existedGroupPerms =
        groupPermissionRepo.findByGroupIdsAndPermissionId(groupIds, permissionId);

    EshopPermission perm = permissionRepo.findById(permissionId).orElseThrow(
        () -> new NoSuchElementException("Not found permission with id = " + permissionId));

    List<GroupPermission> savedGrPerms = eshopGroups.stream()
        .map(gr -> findExistedGroupPerm(existedGroupPerms, gr.getId(), permissionId).map(item -> {
          item.setAllowed(isEnable);
          return item;
        }).orElse(GroupPermission.builder().eshopGroup(gr).eshopPermission(perm).allowed(isEnable)
            .build()))
        .collect(Collectors.toList());

    groupPermissionRepo.saveAll(savedGrPerms);
  }

  private Optional<GroupPermission> findExistedGroupPerm(List<GroupPermission> groupPerms,
      Integer groupId, Integer permId) {
    return groupPerms.stream().filter(groupPerm -> groupId == groupPerm.getEshopGroup().getId()
        && permId == groupPerm.getEshopPermission().getId()).findAny();
  }

  private List<EshopPermission> joinPermissions(List<Integer> defaultPerms,
      List<GroupPermission> customizedPerms) {
    final Set<Integer> actualPermIds = new HashSet<>(defaultPerms);
    if (!CollectionUtils.isEmpty(customizedPerms)) {
      customizedPerms.stream().forEach(p -> {
        if (p.isAllowed()) {
          actualPermIds.add(p.getPermId());
        } else { // exclude the permission
          if (actualPermIds.contains(p.getPermId())) {
            actualPermIds.remove(p.getPermId());
          }
        }
      });
    }
    if (CollectionUtils.isEmpty(actualPermIds)) {
      return Collections.emptyList();
    }
    return permissionRepo.findAllPermissionsIn(actualPermIds);
  }

  private static PermissionDto createPermissionDto(final EshopPermission permission) {
    final PermissionDto permDto = PermissionDto.builder().id(permission.getId())
        .description(permission.getDescription()).permission(permission.getPermission())
        .createdBy(permission.getCreatedBy()).modifiedBy(permission.getModifiedBy()).build();
    permDto.setFunctions(permission.getPermFunctions().stream()
        .map(pf -> createFunctionDto(pf.getEshopFunction())).collect(Collectors.toList()));
    return permDto;
  }

  private static FunctionDto createFunctionDto(final EshopFunction function) {
    return FunctionDto.builder().id(function.getId()).functionName(function.getFunctionName())
        .description(function.getDescription()).relativeUrl(function.getRelativeUrl()).build();
  }

  private BiConsumer<EshopGroup, EshopPermission> getEnablePermissionConsumer() {
    return (eshopGroup, permission) -> {
      Optional<GroupPermission> groupPermission =
          groupPermissionRepo.findByGroupIdAndPermissionId(eshopGroup.getId(), permission.getId());
      if (groupPermission.isPresent()) {
        GroupPermission perm = groupPermission.get();
        perm.setAllowed(true);
        groupPermissionRepo.save(perm);
      } else {
        createGroupPermission(eshopGroup.getId(), permission.getId(), true);
      }
    };
  }

  private BiConsumer<EshopGroup, EshopPermission> getDisablePermissionConsumer() {
    return (eshopGroup, permission) -> {
      Optional<GroupPermission> groupPermission =
          groupPermissionRepo.findByGroupIdAndPermissionId(eshopGroup.getId(), permission.getId());
      if (groupPermission.isPresent()) {
        GroupPermission perm = groupPermission.get();
        perm.setAllowed(false);
        groupPermissionRepo.save(perm);
      } else {
        createGroupPermission(eshopGroup.getId(), permission.getId(), false);
      }
    };
  }

  private void createGroupPermission(int groupId, int permissionId, boolean allowed) {
    EshopGroup groupEntity = eshopGroupRepository.findById(groupId).orElse(null);
    EshopPermission permissionEntity = permissionRepo.findById(permissionId).orElse(null);
    GroupPermission gp = GroupPermission.builder().eshopGroup(groupEntity)
        .eshopPermission(permissionEntity).allowed(allowed).build();
    groupPermissionRepo.save(gp);
  }

  private boolean hasPermission(EshopGroup eshopGroup, Integer permissionId) {
    List<EshopPermission> perms = getPermissionsByGroupId(eshopGroup.getId());
    if (CollectionUtils.isEmpty(perms)) {
      return false;
    }
    return perms.stream().anyMatch(p -> p.getId() == permissionId);
  }

  private List<EshopPermission> getPermissionsByGroupId(int groupId) {
    final List<Integer> defaultPerms =
        permissionRepo.findAllUserDefaultPermissionsByGroupId(groupId);
    final List<GroupPermission> customizedPerms =
        groupPermissionRepo.findAllUserCustomizedPermissionsByGroupId(groupId);
    return joinPermissions(defaultPerms, customizedPerms).stream().collect(Collectors.toList());
  }

  private void reversePermission(EshopGroup eshopGroup, Integer permissionId,
      BiConsumer<EshopGroup, EshopPermission> consumer) {
    EshopPermission permission = permissionRepo.findById(permissionId).orElse(null);

    if (!Objects.isNull(permission)) {
      consumer.accept(eshopGroup, permission);
    } else {
      throw new IllegalArgumentException(PERMISSION_NOT_EXIST);
    }
  }
  // @formatter:on


  private void setPermission(List<EshopGroup> eshopGroups, Integer permissionId, boolean isEnable) {
    eshopGroups.forEach(g -> {
      if (isEnable) {
        reversePermission(g, permissionId, getEnablePermissionConsumer());
      } else {
        reversePermission(g, permissionId, getDisablePermissionConsumer());
      }
    });
  }

  @Override
  public void setPermissions(List<EshopGroup> eshopGroups, List<PermissionEnum> perms,
      boolean isEnable) {
    perms.forEach(perm -> permissionRepo.findPermissionIdByName(perm.name())
        .ifPresent(permId -> setPermission(eshopGroups, permId, isEnable)));
  }

  @Override
  public List<PermissionConfigurationDto> getFinalCustomerMaxPermissions(Long wholesalerUserId) {
    if (wholesalerUserId == null) {
      return Collections.emptyList();
    }
    return getUserPermissions(wholesalerUserId).stream()
        .filter(p -> isValidFinalCustomerPermission(p.getPermission()))
        .map(toPermissionConfigurationDto()).collect(Collectors.toList());
  }

  private Function<PermissionDto, PermissionConfigurationDto> toPermissionConfigurationDto() {
    return p -> PermissionConfigurationDto.builder().permission(p.getPermission())
        .langKey(p.getPermission()).permissionId(p.getId()).enable(true).editable(true).build();
  }

  @Override
  public boolean hasCollectionPermissionByOrgId(Integer orgId, PermissionEnum permission) {
    if (orgId == null || permission == null) {
      return false;
    }
    final Optional<Integer> permissionIdOpt = permissionRepo.findPermissionIdByName(
        permission.name());
    if (!permissionIdOpt.isPresent()) {
      return false;
    }
    return hasPermission(orgId, permissionIdOpt.get());
  }

  @Override
  public Optional<PermissionDto> findByPermission(PermissionEnum permission) {
    if (permission == null) {
      return Optional.empty();
    }
    return permissionRepo.findByPermission(permission.name())
        .map(PermissionServiceImpl::createPermissionDto);
  }

}
