package com.sagag.services.tools.batch;

import com.sagag.services.tools.config.FaultExceptionHandler;
import com.sagag.services.tools.system_variable.SystemVariables;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.Getter;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AbstractStepConfig {

  protected static final int DF_CHUNK = 100;

  @Autowired
  protected SystemVariables sysVars;

  @Autowired(required = false)
  @Qualifier("sourceEntityManager")
  @Getter
  protected EntityManagerFactory sourceEntityManagerFactory;

  @Autowired
  protected PlatformTransactionManager sourceTransactionManager;

  @Autowired
  @Qualifier("targetEntityManager")
  @Getter
  protected EntityManagerFactory targetEntityManagerFactory;

  @Autowired
  @Qualifier("targetEntityManager")
  protected EntityManager targetEntityManager;

  @Autowired
  protected PlatformTransactionManager targetTransactionManager;

  @Autowired
  protected StepBuilderFactory stepBuilderFactory;

  @Autowired
  protected JobRepository jobRepository;

  protected Step toStep(Tasklet tasklet) {
    return taskletStepBuilder(tasklet).build();
  }

  protected TaskletStepBuilder taskletStepBuilder(Tasklet tasklet) {
    TaskletStepBuilder taskletStepBuilder = stepBuilderFactory
        .get(tasklet.getClass().getSimpleName()).tasklet(tasklet);
    taskletStepBuilder.repository(jobRepository);
    taskletStepBuilder.transactionManager(targetTransactionManager);
    taskletStepBuilder.exceptionHandler(new FaultExceptionHandler());
    return taskletStepBuilder;
  }

  protected <I, O> SimpleStepBuilder<I, O> stepBuilder(String name, int chunk) {
    return stepBuilderFactory.get(name).<I, O>chunk(chunk);
  }

  protected <I, O> SimpleStepBuilder<I, O> stepBuilder(String name) {
    return stepBuilderFactory.get(name).<I, O>chunk(DF_CHUNK);
  }

  protected <T> JpaPagingItemReader<T> jpaPagingItemReader(EntityManagerFactory emFactory,
      JpaQueryProvider queryProvider) throws Exception {
    return jpaPagingItemReader(emFactory, queryProvider, ToolConstants.MAX_SIZE);
  }

  protected <T> JpaPagingItemReader<T> jpaPagingItemReader(EntityManagerFactory emFactory,
      JpaQueryProvider queryProvider, int pageSize) throws Exception {
    final JpaPagingItemReader<T> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(emFactory);
    reader.setPageSize(pageSize);
    reader.setQueryProvider(queryProvider);
    reader.afterPropertiesSet();
    return reader;
  }
}
