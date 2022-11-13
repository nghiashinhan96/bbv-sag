package com.sagag.services.tools.service;

import com.sagag.services.tools.domain.target.EshopGroup;
import com.sagag.services.tools.support.PermissionEnum;

import java.util.List;

/**
 * Interface to define user permission service.
 */
public interface PermissionService {

  /**
   * set permissions for each user group with given state.
   * 
   * @param eshopGroups groups that need to set permission
   * @param perms permissions will set to each user group
   * @param isEnable true if you want to set permission enable otherwise It will set permission
   *        disable
   */
  public void setPermissions(List<EshopGroup> eshopGroups, List<PermissionEnum> perms,
      boolean isEnable);

}
