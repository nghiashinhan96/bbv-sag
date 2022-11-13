package com.sagag.eshop.ci.bamboo.specs.permission;

import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;

public class BuildAndDeploymentPlanPermissionsBuilder implements IPlanPermissionsBuilder {

  @Override
  public PlanPermissions buildPlanPermission() {
    final PlanPermissions planPermission =
        new PlanPermissions(new PlanIdentifier("CONNECT", "RLAX")).permissions(new Permissions()
            .userPermissions("ext_thi", PermissionType.EDIT, PermissionType.VIEW,
                PermissionType.ADMIN, PermissionType.CLONE, PermissionType.BUILD)
            .userPermissions("ext_tran", PermissionType.VIEW, PermissionType.EDIT,
                PermissionType.BUILD, PermissionType.ADMIN, PermissionType.CLONE)
            .userPermissions("ext_kiet", PermissionType.EDIT, PermissionType.VIEW,
                PermissionType.ADMIN, PermissionType.CLONE, PermissionType.BUILD)
            .loggedInUserPermissions(PermissionType.VIEW, PermissionType.BUILD)
            .anonymousUserPermissionView());
    return planPermission;
  }

}
