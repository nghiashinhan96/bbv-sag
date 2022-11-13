package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.service.dto.FunctionDto;
import com.sagag.eshop.service.dto.PermissionDto;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;

import java.util.List;
import java.util.Optional;

/**
 * Interface to define user permission service.
 */
public interface PermissionService {

  /**
   * Returns all available permissions for a specific user.
   *
   * @param userId the user to request
   * @return a list of permissions with accessible functions for that user.
   */
  List<PermissionDto> getUserPermissions(long userId);

  /**
   * Returns all available permissions for a specific group.
   *
   * @param groupId group's id which we want to get permission
   * @return a list of permissions with accessible functions for that group.
   */
  List<PermissionDto> getUserPermissionsByGroupId(int groupId);

  /**
   * Check whether functions has url permission or not.
   *
   * @param functions list function to check
   * @param url relative url to check
   * @return result that functions has permission?
   */
  boolean hasUrlPermission(List<FunctionDto> functions, String url);

  /**
   * check whether eshopGroups has specific permission or not
   *
   * @param eshopGroups
   * @param permissionId
   * @return
   */
  boolean hasPermission(List<EshopGroup> eshopGroups, Integer permissionId);

  /**
   * check whether customer has specific permission or not.
   *
   * @param orgId id of customer
   * @param permissionId id of permission
   * @return
   */
  boolean hasPermission(Integer orgId, Integer permissionId);

  /**
   * update specific permission for eshopGroups
   *
   * @param eshopGroups
   * @param permissionId
   * @param isEnable
   */
  void updatePermission(List<EshopGroup> eshopGroups, Integer permissionId,
      boolean isEnable);

  /**
   * set permissions for each user group with given state.
   *
   * @param eshopGroups groups that need to set permission
   * @param perms permissions will set to each user group
   * @param isEnable true if you want to set permission enable otherwise It will set permission
   *        disable
   */
  void setPermissions(List<EshopGroup> eshopGroups, List<PermissionEnum> perms,
      boolean isEnable);

  /**
   * Returns maximum permission for final customer.
   *
   * @param wholesalerUserId user of wholesaler whom final customer belong to
   * @return
   */
  List<PermissionConfigurationDto> getFinalCustomerMaxPermissions(Long wholesalerUserId);

  boolean hasCollectionPermissionByOrgId(Integer orgId, PermissionEnum permission);

  Optional<PermissionDto> findByPermission(PermissionEnum permission);
}
