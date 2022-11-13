package com.sagag.services.common.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class to implement thread manager.
 *
 */
@Component
@Slf4j
public class ThreadManager {

  @Autowired
  @Qualifier("executorService")
  private ExecutorService executorService;

  @Autowired
  @Qualifier("scheduledExecutorService")
  private ScheduledExecutorService scheduledExecutorService;

  @Value("${sag.executor.threadTimeout: 10000}")
  private Integer threadTimeout;

  public <T> CompletableFuture<T> supplyAsync(Callable<T> task, Supplier<T> defaultVal) {
    return CompletableFuture.supplyAsync(() -> createAsyncJob(task, false, defaultVal));
  }

  public <T> CompletableFuture<T> supplyAsync(Callable<T> task, T defaultVal) {
    return CompletableFuture.supplyAsync(() -> createAsyncJob(task, false, () -> defaultVal));
  }

  public CompletableFuture<Void> supplyAsyncVoid(Callable<Void> task) {
    return CompletableFuture.supplyAsync(() -> createAsyncJob(task, true, null));
  }

  private <T> T createAsyncJob(Callable<T> task, boolean isVoidObj, Supplier<T> defaultVal) {
    final Future<T> future = executorService.submit(task);
    scheduledExecutorService.schedule(() -> future.cancel(true), threadTimeout,
        TimeUnit.MILLISECONDS);
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException | CancellationException ex) {
      log.error("Error while createAsyncJob", ex);
      if (isVoidObj) {
        return null;
      }
      return defaultVal.get();
    }
  }

}
