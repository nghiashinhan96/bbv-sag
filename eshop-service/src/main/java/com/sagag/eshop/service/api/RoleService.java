package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.services.domain.eshop.dto.EshopRoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {

  /**
   * Returns all available {@link EshopRole}s.
   *
   * @return a list of roles.
   */
  List<EshopRole> getAllRoles();

  /**
   * Returns all {@link EshopRole}s from a list of role <code>ids</code>.
   *
   * @param ids a list of role ids.
   * @return a list of roles.
   */
  List<EshopRole> findAllRolesInIds(List<Integer> ids);

  /**
   * Returns a {@link EshopRole}.
   *
   * @return a role.
   */
  Optional<EshopRole> findRoleByName(String name);

  /**
   *
   * @return a {@link EshopRoleDto}}
   */
  List<EshopRoleDto> getAllRoleDto();

  List<EshopRoleDto> getEshopRolesForUserProfile(String userRole, boolean isOtherProfile);

  List<EshopRoleDto> getDefaultEshopRoles();
}
