package com.sagag.eshop.ci.bamboo.specs.permission;

import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;

public class TriggerPlanPermissionsBuilder implements IPlanPermissionsBuilder {

  @Override
  public PlanPermissions buildPlanPermission() {
    final PlanPermissions planPermission =
        new PlanPermissions(new PlanIdentifier("CONNECT", "TRGAX")).permissions(new Permissions()
            .userPermissions("ext_thi", PermissionType.ADMIN, PermissionType.VIEW,
                PermissionType.CLONE, PermissionType.BUILD, PermissionType.EDIT)
            .userPermissions("ext_kiet", PermissionType.VIEW, PermissionType.CLONE,
                PermissionType.EDIT, PermissionType.ADMIN, PermissionType.BUILD)
            .userPermissions("ext_tran", PermissionType.ADMIN, PermissionType.EDIT,
                PermissionType.BUILD, PermissionType.CLONE, PermissionType.VIEW)
            .loggedInUserPermissions(PermissionType.VIEW).anonymousUserPermissionView());
    return planPermission;
  }

}
