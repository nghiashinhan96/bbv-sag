package com.sagag.services.tools.analysis;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.FaultExceptionHandler;
import com.sagag.services.tools.config.OracleProfile;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;

/**
 *
 */
@Configuration
@OracleProfile
public class ExportCsvFileFromSourceRepoJobConfig extends AbstractJobConfig {

  @Autowired
  private ExportShopArticlesFromOracleDBTasklet exportShopArticlesFromOracleDBTasklet;

  @Autowired
  private ExportPersonDataFromOracleDBTasklet exportPersonDataFromOracleDBTasklet;

  @Autowired
  private ExportPersonPropertyFromOracleDBTasklet exportPersonPropertyFromOracleDBTasklet;

  @Autowired
  private FilteringFinalCustomerFromCsvTasklet filteringFinalCustomerFromCsvTasklet;

  @Override
  protected String jobName() {
    return StringUtils.EMPTY;
  }

  @Bean
  @Transactional
  public Job exportShopArticlesJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("exportShopArticlesJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(exportShopArticlesFromOrclDBStep()).build();
  }

  @Bean
  public Step exportShopArticlesFromOrclDBStep() {
    TaskletStepBuilder taskletStepBuilder = stepBuilderFactory.get("exportShopArticlesFromOrclDBStep")
      .tasklet(exportShopArticlesFromOracleDBTasklet);
    taskletStepBuilder.repository(jobRepository);
    taskletStepBuilder.transactionManager(sourceTransactionManager);
    taskletStepBuilder.exceptionHandler(new FaultExceptionHandler());
    return taskletStepBuilder.build();
  }

  @Bean
  @Transactional
  public Job exportPersonJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("exportPersonListJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(exportPersonFromOrclDBStep()).build();
  }

  @Bean
  public Step exportPersonFromOrclDBStep() {
    TaskletStepBuilder taskletStepBuilder = stepBuilderFactory.get("exportPersonFromOrclDBStep")
      .tasklet(exportPersonDataFromOracleDBTasklet);
    taskletStepBuilder.repository(jobRepository);
    taskletStepBuilder.transactionManager(sourceTransactionManager);
    taskletStepBuilder.exceptionHandler(new FaultExceptionHandler());
    return taskletStepBuilder.build();
  }

  @Bean
  @Transactional
  public Job exportPersonPropertyJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("exportPersonPropertyListJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(exportPersonPropertiesFromOrclDBStep()).build();
  }

  @Bean
  public Step exportPersonPropertiesFromOrclDBStep() {
    TaskletStepBuilder taskletStepBuilder = stepBuilderFactory.get("exportPersonPropertiesFromOrclDBStep")
      .tasklet(exportPersonPropertyFromOracleDBTasklet);
    taskletStepBuilder.repository(jobRepository);
    taskletStepBuilder.transactionManager(sourceTransactionManager);
    taskletStepBuilder.exceptionHandler(new FaultExceptionHandler());
    return taskletStepBuilder.build();
  }

  @Bean
  @Transactional
  public Job filterFinalCustomersJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("filterFinalCustomersJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(filteringFinalCustomerFromOrclDBStep()).build();
  }

  @Bean
  public Step filteringFinalCustomerFromOrclDBStep() {
    TaskletStepBuilder taskletStepBuilder = stepBuilderFactory.get("filteringFinalCustomerFromOrclDBStep")
      .tasklet(filteringFinalCustomerFromCsvTasklet);
    taskletStepBuilder.repository(jobRepository);
    taskletStepBuilder.transactionManager(sourceTransactionManager);
    taskletStepBuilder.exceptionHandler(new FaultExceptionHandler());
    return taskletStepBuilder.build();
  }

}
