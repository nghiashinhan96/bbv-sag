package com.sagag.services.common.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

/**
 * Configuration for thread manager when create async jobs.
 */
@Configuration
@Slf4j
public class ExecutorConfiguration implements AsyncConfigurer {

  @Value("${sag.executor.corePoolSize: 100}")
  private int corePoolSize;

  @Value("${sag.executor.maximumPoolSize: 100}")
  private int maximumPoolSize;

  @Value("${sag.executor.scheduledPoolSize: 5}")
  private int scheduledPoolSize;

  @Value("${sag.executor.keepAliveTime: 5000}")
  private long keepAliveTime;

  @Bean(name = "executorService")
  public ExecutorService executorService() {
    log.info("Creating executor service with core-pool-size = {}, maximum-pool-size = {}"
        + ", scheduled-pool-size = {}, keep-alive-time {}",
        corePoolSize, maximumPoolSize, scheduledPoolSize, keepAliveTime);

    final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
        keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    poolExecutor.allowCoreThreadTimeOut(true);
    return poolExecutor;
  }

  @Bean(name = "scheduledExecutorService")
  public ScheduledExecutorService scheduledExecutorService() {
    return Executors.newScheduledThreadPool(scheduledPoolSize);
  }

  @Override
  public Executor getAsyncExecutor() {
    final int corePoolSizeAsync = 20;
    final int maxPoolSizeAsync = 100;
    log.info("Creating asynchronous executor service with core-pool-size = {}, " +
            "maximum-pool-size = {}, keep-alive-time {}",
        corePoolSizeAsync, maxPoolSizeAsync, keepAliveTime);

    final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
        corePoolSizeAsync, maxPoolSizeAsync, keepAliveTime, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>());
    poolExecutor.allowCoreThreadTimeOut(true);
    return poolExecutor;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return (ex, method, params) -> log.error("Async uncaught exception handler :", ex);
  }
}
