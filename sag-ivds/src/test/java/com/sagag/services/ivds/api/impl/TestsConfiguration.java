package com.sagag.services.ivds.api.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Configuration class for test purpose only.
 */
@Configuration
@Profile("test")
public class TestsConfiguration {

  @Value("${sag.executor.corePoolSize: 100}")
  private Integer corePoolSize;

  @Value("${sag.executor.maximumPoolSize: 100}")
  private Integer maximumPoolSize;

  @Value("${sag.executor.scheduledPoolSize: 5}")
  private Integer scheduledPoolSize;

  @Value("${sag.executor.keepAliveTime: 5000}")
  private Integer keepAliveTime;

  @Bean(name = "executorService")
  public ExecutorService executorService() {
    final ThreadPoolExecutor poolExecutor =
        new ThreadPoolExecutor(corePoolSize, maximumPoolSize, Long.valueOf(keepAliveTime),
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    poolExecutor.allowCoreThreadTimeOut(true);
    return poolExecutor;
  }

  @Bean(name = "scheduledExecutorService")
  public ScheduledExecutorService scheduledExecutorService() {
    return Executors.newScheduledThreadPool(scheduledPoolSize);
  }
}
