package com.sagag.services.copydb.batch;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.Getter;

public class AbstractJobConfig {

  protected static final int DF_CHUNK = 1000;

  @Autowired
  @Qualifier("sourceEntityManager")
  @Getter
  private EntityManagerFactory entityManagerFactory;
  
  @Autowired
  protected JobBuilderFactory jobBuilderFactory;

  @Autowired
  protected StepBuilderFactory stepBuilderFactory;

  @Autowired
  protected JobRepository jobRepository;

  @Autowired
  protected TaskExecutor taskExecutor;

  @Autowired
  protected PlatformTransactionManager targetTransactionManager;

  @Autowired
  protected PlatformTransactionManager sourceTransactionManager;

  protected Step toStep(Tasklet tasklet) {
    TaskletStepBuilder taskletStepBuilder = stepBuilderFactory.get(tasklet.getClass().getSimpleName())
      .tasklet(tasklet);
    taskletStepBuilder.repository(jobRepository);
    taskletStepBuilder.transactionManager(targetTransactionManager);
    taskletStepBuilder.exceptionHandler(new FaultExceptionHandler());
    return taskletStepBuilder.build();
  }
}
