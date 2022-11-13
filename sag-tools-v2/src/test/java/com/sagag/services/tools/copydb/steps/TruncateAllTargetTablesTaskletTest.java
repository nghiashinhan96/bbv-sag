package com.sagag.services.tools.copydb.steps;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TruncateAllTargetTablesTaskletTest {

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  @Qualifier("TruncateAllTargetTables")
  protected Job jobTruncateAllTargetTables;


  @Test
  public void executeJob() throws Exception {
    log.info("Execute job name {}", jobTruncateAllTargetTables.getName());
    JobExecution execution = jobLauncher.run(jobTruncateAllTargetTables, new JobParameters());
    log.info("JobExecution status: {}", execution.getStatus());
    Assert.assertThat(execution.getExitStatus(), Matchers.is(ExitStatus.COMPLETED));
  }
}
