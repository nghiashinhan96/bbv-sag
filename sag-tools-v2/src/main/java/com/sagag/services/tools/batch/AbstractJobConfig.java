package com.sagag.services.tools.batch;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;

public abstract class AbstractJobConfig extends AbstractStepConfig {

  @Autowired
  protected JobBuilderFactory jobBuilderFactory;

  @Autowired
  protected TaskExecutor taskExecutor;

  @Autowired
  private RestoreDbConfigTasklet restoreDbConfigTasklet;

  @Autowired
  private BatchJobCompletionNotificationListener listener;

  protected abstract String jobName();

  @Bean
  protected Step restoreDbConfigStep() {
    return toStep(restoreDbConfigTasklet);
  }

  protected JobBuilder jobBuilder(JobExecutionListener listener) {
    return jobBuilder(StringUtils.EMPTY, listener);
  }

  protected JobBuilder jobBuilder() {
    return jobBuilder(StringUtils.EMPTY, listener);
  }

  protected JobBuilder jobBuilder(String jobName) {
    return jobBuilder(jobName, listener);
  }

  private JobBuilder jobBuilder(String jobName, JobExecutionListener listener) {
    return jobBuilderFactory
      .get(StringUtils.defaultIfBlank(jobName, jobName()))
      .listener(listener)
      .incrementer(new RunIdIncrementer());
  }
}
