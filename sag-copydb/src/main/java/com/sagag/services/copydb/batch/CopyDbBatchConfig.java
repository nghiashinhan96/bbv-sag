package com.sagag.services.copydb.batch;

import javax.sql.DataSource;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.sagag.services.copydb.config.CopyDbProfile;

/**
 * Batch job configurations.
 */
@Configuration
@EnableBatchProcessing
@CopyDbProfile
public class CopyDbBatchConfig {

  /**
   * Constructs the {@link TaskExecutor} for indexing job.
   *
   * @return the {@link ThreadPoolTaskExecutor}
   */
  @Bean("taskExecutor")
  @Primary
  public TaskExecutor taskExecutor() {
    final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(10);
    threadPoolTaskExecutor.setMaxPoolSize(100);
    threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    threadPoolTaskExecutor.setAwaitTerminationSeconds(30);
    threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
    threadPoolTaskExecutor.setKeepAliveSeconds(60);
    threadPoolTaskExecutor.setQueueCapacity(10000);
    threadPoolTaskExecutor.setThreadNamePrefix("BatchTaskExecutor-");
    return threadPoolTaskExecutor;
  }

  /**
   * Constructs the {@link org.springframework.batch.core.launch.JobLauncher} with
   * thread pool task executor.
   *
   * @return the {@link SimpleJobLauncher}
   * @throws Exception thrown when the initialization fails.
   */
  @Bean("jobLauncher")
  @Primary
  public SimpleJobLauncher jobLauncher(PlatformTransactionManager targetTransactionManager,
      @Qualifier(value = "targetDataSource") DataSource targetDataSource) throws Exception {
    final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(jobRepository(targetTransactionManager, targetDataSource));
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
  public JobRepository jobRepository(PlatformTransactionManager targetTransactionManager,
      @Qualifier(value = "targetDataSource") DataSource targetDataSource) throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(targetDataSource);
    factory.setTransactionManager(targetTransactionManager);
    factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
    factory.setValidateTransactionState(false);
    factory.setMaxVarCharLength(Integer.MAX_VALUE);
    factory.setTablePrefix("BATCH_");
    factory.setDatabaseType("SQLSERVER");
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean(name = "dozerBeanMapper")
  @Primary
  public DozerBeanMapper dozerBeanMapper() {
    return new DozerBeanMapper();
  }

}
