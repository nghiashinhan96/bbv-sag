package com.sagag.eshop.ci.bamboo.specs.plan;

import java.time.Duration;

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
import com.atlassian.bamboo.specs.builders.task.MavenTask;
import com.atlassian.bamboo.specs.builders.task.NpmTask;
import com.atlassian.bamboo.specs.builders.task.ScriptTask;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.builders.trigger.BitbucketServerTrigger;
import com.atlassian.bamboo.specs.builders.trigger.RepositoryPollingTrigger;
import com.atlassian.bamboo.specs.model.task.ScriptTaskProperties;
import com.atlassian.bamboo.specs.util.MapBuilder;

public class BuildAndDeploymentPlanBuilder implements IPlanBuilder {

  @Override
  public Plan buildPlan() {
    final Plan plan =
        new Plan(
            new Project()
                .oid(new BambooOid("1x21rtt5huyo5")).key(new BambooKey("CONNECT")).name(
                    "SAG-Connect-Webshops"),
            "03_BUILD_SAG_AX", new BambooKey("RLAX")).oid(new BambooOid("1x1s2m7sa16b5"))
                .description("Build and Deploy The SAG AX Branch")
                .pluginConfigurations(new ConcurrentBuilds(), new AllOtherPluginsConfiguration()
                    .configuration(new MapBuilder<String, Object>().put("custom.buildExpiryConfig",
                        new MapBuilder<String, Object>().put("duration", "1").put("period", "days")
                            .put("labelsToKeep", "ax").put("expiryTypeResult", "true")
                            .put("buildsToKeep", "2").put("enabled", "true").build())
                        .build()))
                .stages(new Stage("Default Stage")
                    .jobs(new Job("Default Job", new BambooKey("JOB1"))
                        .pluginConfigurations(
                            new AllOtherPluginsConfiguration()
                                .configuration(new MapBuilder<String, Object>()
                                    .put("custom", new MapBuilder<String, Object>()
                                        .put("auto",
                                            new MapBuilder<String, Object>().put("regex", "")
                                                .put("label", "").build())
                                        .put("buildHangingConfig.enabled", "false")
                                        .put("ncover.path", "")
                                        .put("clover",
                                            new MapBuilder<String, Object>()
                                                .put("path", "").put("license", "")
                                                .put("useLocalLicenseKey", "true").build())
                                        .build())
                                    .build()))
                        .artifacts(new Artifact().name("ax").copyPattern("**/*.*").shared(true))
                        .tasks(new VcsCheckoutTask()
                            .description("Checkout")
                            .checkoutItems(new CheckoutItem().defaultRepository()),
                            new ScriptTask().description("Install Node Modules For Web Client")
                                .enabled(false)
                                .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
                                .inlineBody(
                                    "npm -v\n\nls -a\ncd eshop-web/src/main/resources/client\nrm -rf ./node_modules\nnpm cache verify\nnpm install"),
                            new NpmTask().description("Build Eshop Client").enabled(false)
                                .nodeExecutable("NodeJs")
                                .environmentVariables("--max_old_space_size=4096")
                                .workingSubdirectory("/eshop-web/src/main/resources/client/")
                                .command("run build--ci"),
                            new ScriptTask().description("Install Node Modules For Web Back Office")
                                .enabled(false)
                                .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
                                .inlineBody("rm -rf ./node_modules\nnpm cache verify\nnpm install")
                                .workingSubdirectory(
                                    "eshop-backoffice/src/main/resources/backoffice"),
                            new NpmTask().description("Build Back Office").enabled(false)
                                .nodeExecutable("NodeJs")
                                .workingSubdirectory(
                                    "eshop-backoffice/src/main/resources/backoffice")
                                .command("run build--ci"),
                            new MavenTask()
                                .description("Build Maven").goal("-U clean compile test")
                                .jdk(
                                    "JDK8 u92")
                                .executableLabel("mvn3").hasTests(true)
                                .workingSubdirectory("/eshop-parent"))
                        .finalTasks(new ScriptTask().description("Clean Back Office")
                            .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
                            .inlineBody(
                                "rm -rf eshop-backoffice/src/main/resources/backoffice/node_modules\nrm -rf eshop-backoffice/src/main/resources/public"),
                            new ScriptTask().description("Clean sources Web client")
                                .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
                                .inlineBody(
                                    "rm -rf eshop-web/src/main/resources/client/node_modules\nrm -rf eshop-web/src/main/resources/public\ncd ./eshop-parent\n/usr/share/apache-maven/bin/mvn clean"))
                        .cleanWorkingDirectory(true)))
                .linkedRepositories("SAG-CONNECT-AX")

                .triggers(new RepositoryPollingTrigger().withPollingPeriod(Duration.ofSeconds(60)),
                    new BitbucketServerTrigger())
                .planBranchManagement(new PlanBranchManagement().createForVcsBranch()
                    .delete(new BranchCleanup().whenRemovedFromRepositoryAfterDays(1))
                    .notificationLikeParentPlan().issueLinkingEnabled(false));
    return plan;
  }
}
