package com.sagag.eshop.ci.bamboo.specs;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.util.BambooServer;
import com.sagag.eshop.ci.bamboo.specs.permission.BuildAndDeploymentPlanPermissionsBuilder;
import com.sagag.eshop.ci.bamboo.specs.permission.TriggerPlanPermissionsBuilder;
import com.sagag.eshop.ci.bamboo.specs.plan.BuildAndDeploymentPlanBuilder;
import com.sagag.eshop.ci.bamboo.specs.plan.TriggerPlanBuilder;

/**
 * Plan configuration for Bamboo.
 *
 * <pre>This is the alpha version for new CI/CD mechanism for us, Not impact to current source code.</pre>
 *
 * @see <a href="https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs">Bamboo Specs</a>
 */
@BambooSpec
public class PlanSpec {

  private static final String BAMBOO_SERVER = "https://bamboo.sag-ag.ch";

  public static void main(String... argv) {
    // By default credentials are read from the '.credentials' file.
    final BambooServer bambooServer = new BambooServer(BAMBOO_SERVER);

    bambooServer.publish(new BuildAndDeploymentPlanBuilder().buildPlan());
    bambooServer.publish(new BuildAndDeploymentPlanPermissionsBuilder().buildPlanPermission());

    bambooServer.publish(new TriggerPlanBuilder().buildPlan());
    bambooServer.publish(new TriggerPlanPermissionsBuilder().buildPlanPermission());
  }
}
