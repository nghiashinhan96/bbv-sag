package com.sagag.services.common.schedule;

/**
 * Functional interface to provide scheduled task for Connect App.
 *
 */
@FunctionalInterface
public interface ScheduledTask {

  /**
   * Executes the task is implemented by schedule for business.
   */
  void executeTask();
}
