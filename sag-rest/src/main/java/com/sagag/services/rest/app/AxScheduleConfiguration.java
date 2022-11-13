package com.sagag.services.rest.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Configuration class to build schedule for Spring Scheduler.
 *
 */
@Configuration
@PropertySource("classpath:cronjob/cron.properties")
public class AxScheduleConfiguration implements SchedulingConfigurer {

  @Value("${common.thread_pool_size.max}")
  private int maxThreadPoolSize;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(taskExecutor());
  }

  @Bean(destroyMethod = "shutdown")
  public Executor taskExecutor() {
    return Executors.newScheduledThreadPool(maxThreadPoolSize);
  }
}
