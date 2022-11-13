package com.sagag.services.tools.service.impl;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.EshopGroup;
import com.sagag.services.tools.domain.target.EshopPermission;
import com.sagag.services.tools.domain.target.GroupPermission;
import com.sagag.services.tools.repository.target.EshopGroupRepository;
import com.sagag.services.tools.repository.target.EshopPermissionRepository;
import com.sagag.services.tools.repository.target.GroupPermissionRepository;
import com.sagag.services.tools.service.PermissionService;
import com.sagag.services.tools.support.PermissionEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Permission implementation class.
 */
@Service
@Transactional
@OracleProfile
public class PermissionServiceImpl implements PermissionService {

  @Autowired
  private EshopPermissionRepository permissionRepo;

  @Autowired
  private GroupPermissionRepository groupPermissionRepo;

  @Autowired
  private EshopGroupRepository eshopGroupRepository;

  private static final String PERMISSION_NOT_EXIST = "Permission does not exist.";

  @Override
  public void setPermissions(List<EshopGroup> eshopGroups, List<PermissionEnum> perms, boolean isEnable) {
    perms.forEach(perm -> permissionRepo.findPermissionIdByName(perm.name()).ifPresent(permId -> setPermission(eshopGroups, permId, isEnable)));
  }

  private void setPermission(List<EshopGroup> eshopGroups, Integer permissionId, boolean isEnable) {
    eshopGroups.forEach(g -> {
      if (isEnable) {
        reversePermission(g, permissionId, getEnablePermissionConsumer());
      } else {
        reversePermission(g, permissionId, getDisablePermissionConsumer());
      }
    });
  }

  private BiConsumer<EshopGroup, EshopPermission> getEnablePermissionConsumer() {
    return (eshopGroup, permission) -> {
      Optional<GroupPermission> groupPermission = groupPermissionRepo.findByGroupIdAndPermissionId(eshopGroup.getId(), permission.getId());
      if (groupPermission.isPresent()) {
        GroupPermission perm = groupPermission.get();
        perm.setAllowed(true);
        groupPermissionRepo.save(perm);
      } else {
        createGroupPermission(eshopGroup.getId(), permission.getId(), true);
      }
    };
  }

  private void createGroupPermission(int groupId, int permissionId, boolean allowed) {
    EshopGroup groupEntity = eshopGroupRepository.findById(groupId).orElse(null);
    EshopPermission permissionEntity = permissionRepo.findById(permissionId).orElse(null);
    GroupPermission gp = GroupPermission.builder().eshopGroup(groupEntity).eshopPermission(permissionEntity).allowed(allowed).build();
    groupPermissionRepo.save(gp);
  }

  private BiConsumer<EshopGroup, EshopPermission> getDisablePermissionConsumer() {
    return (eshopGroup, permission) -> {
      Optional<GroupPermission> groupPermission = groupPermissionRepo.findByGroupIdAndPermissionId(eshopGroup.getId(), permission.getId());
      if (groupPermission.isPresent()) {
        GroupPermission perm = groupPermission.get();
        perm.setAllowed(false);
        groupPermissionRepo.save(perm);
      } else {
        createGroupPermission(eshopGroup.getId(), permission.getId(), false);
      }
    };
  }

  private void reversePermission(EshopGroup eshopGroup, Integer permissionId, BiConsumer<EshopGroup, EshopPermission> consumer) {
    EshopPermission permission = permissionRepo.findById(permissionId).orElse(null);

    if (!Objects.isNull(permission)) {
      consumer.accept(eshopGroup, permission);
    } else {
      throw new IllegalArgumentException(PERMISSION_NOT_EXIST);
    }
  }

}
