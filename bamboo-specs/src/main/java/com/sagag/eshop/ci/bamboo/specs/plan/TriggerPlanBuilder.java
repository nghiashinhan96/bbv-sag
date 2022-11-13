package com.sagag.eshop.ci.bamboo.specs.plan;

import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.BambooOid;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact;
import com.atlassian.bamboo.specs.api.builders.plan.branches.BranchCleanup;
import com.atlassian.bamboo.specs.api.builders.plan.branches.PlanBranchManagement;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.AllOtherPluginsConfiguration;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.ConcurrentBuilds;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.builders.task.CheckoutItem;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.builders.trigger.RepositoryPollingTrigger;
import com.atlassian.bamboo.specs.util.MapBuilder;

public class TriggerPlanBuilder implements IPlanBuilder {

  @Override
  public Plan buildPlan() {
    final Plan plan =
        new Plan(
            new Project().oid(new BambooOid("1x21rtt5huyo5")).key(new BambooKey("CONNECT"))
                .name("SAG-Connect-Webshops"),
            "03_TRIGGER_SAG_AX", new BambooKey("TRGAX"))
                .oid(new BambooOid("1x1s2m7sa16bh"))
                .pluginConfigurations(new ConcurrentBuilds(), new AllOtherPluginsConfiguration()
                    .configuration(new MapBuilder<String, Object>().put("custom.buildExpiryConfig",
                        new MapBuilder<String, Object>().put("duration", "1").put("period", "days")
                            .put("labelsToKeep", "trigger-non-dvse").put("expiryTypeResult", "true")
                            .put("buildsToKeep", "1").put("enabled", "true").build())
                        .build()))
                .stages(
                    new Stage("Default Stage").jobs(new Job("Default Job", new BambooKey("JOB1"))
                        .artifacts(new Artifact().name("refresh-cache-ax").copyPattern("**/*.*")
                            .shared(true))
                        .tasks(new VcsCheckoutTask().description("Checkout Default Repository")
                            .checkoutItems(new CheckoutItem().defaultRepository()))))
                .linkedRepositories("SAG-CONNECT-AX")

                .triggers(new RepositoryPollingTrigger().enabled(false))
                .planBranchManagement(new PlanBranchManagement().delete(new BranchCleanup())
                    .notificationForCommitters());
    return plan;
  }

}
