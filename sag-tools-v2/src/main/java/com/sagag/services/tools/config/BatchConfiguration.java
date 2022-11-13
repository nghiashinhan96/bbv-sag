package com.sagag.services.tools.config;

import com.sagag.services.tools.support.AvailablecusCustomerResource;
import com.sagag.services.tools.support.CommonInitialResource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

import javax.sql.DataSource;

/**
 * Batch job configurations.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  private DataSource targetDataSource;

  @Autowired
  private PlatformTransactionManager targetTransactionManager;

  /**
   * Constructs the {@link TaskExecutor} for indexing job.
   *
   * @return the {@link ThreadPoolTaskExecutor}
   */
  @Bean("taskExecutor")
  @Primary
  public TaskExecutor taskExecutor() {
    final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(1);
    threadPoolTaskExecutor.setMaxPoolSize(100);
    threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    threadPoolTaskExecutor.setAwaitTerminationSeconds(60);
    threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
    threadPoolTaskExecutor.setKeepAliveSeconds(60);
    threadPoolTaskExecutor.setQueueCapacity(10);
    threadPoolTaskExecutor.setThreadNamePrefix("DaemonTaskExecutor-");
    return threadPoolTaskExecutor;
  }

  /**
   * Constructs the {@link org.springframework.batch.core.launch.JobLauncher} with thread pool task executor.
   *
   * @return the {@link SimpleJobLauncher}
   * @throws Exception thrown when the initialization fails.
   */
  @Bean("jobLauncher")
  @Primary
  public SimpleJobLauncher jobLauncher() throws Exception {
    final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(jobRepository());
    jobLauncher.setTaskExecutor(taskExecutor());
    return jobLauncher;
  }

  /**
   * Constructs the {@link JobRepository}.
   *
   * @return the {@link JobRepository}
   */
  @Bean("jobRepository")
  @Primary
  public JobRepository jobRepository() throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(targetDataSource);
    factory.setTransactionManager(targetTransactionManager);
    factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
    factory.setValidateTransactionState(false);
    factory.setTablePrefix("BATCH_");
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  /**
   * Constructs the {@link CommonInitialResource} to load common resource before progressing.
   *
   * @return the {@link CommonInitialResource}
   */
  @Bean("commonInitialResource")
  @OracleProfile
  public CommonInitialResource commonInitialResource() throws Exception {
    return new CommonInitialResource();
  }

  @Bean("availablecusCustomerResource")
  @OracleProfile
  public AvailablecusCustomerResource availablecusCustomerResource() {
    return new AvailablecusCustomerResource(Collections.emptyList());
  }

}
