package com.sagag.services.copydb.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;

/**
 * Fault exception handler.
 */
@Slf4j
public class FaultExceptionHandler implements ExceptionHandler {

  @Override
  public void handleException(RepeatContext context, Throwable throwable) {
    log.error("Exit when has error", throwable);
    log.info("!!! STOPPED APP !!!");
    System.exit(0);
  }
}
