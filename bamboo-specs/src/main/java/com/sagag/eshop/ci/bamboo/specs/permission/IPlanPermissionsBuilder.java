package com.sagag.eshop.ci.bamboo.specs.permission;

import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;

@FunctionalInterface
public interface IPlanPermissionsBuilder {

  PlanPermissions buildPlanPermission();

}
