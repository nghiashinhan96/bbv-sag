package com.sagag.services.ax.enums;

/**
 * Enumeration class to define batch job status from AX.
 */
public enum AxBatchJobStatus {
  HOLD, WAITING, EXECUTING, ERROR, FINISHED, READY, NOTRUN, CANCELLING, CANCELED, SCHEDULED;

  public boolean isSuccess() {
    return this == FINISHED;
  }

  public boolean isError() {
    return this == ERROR;
  }

  public boolean isCanceled() {
    return this == CANCELED;
  }

  public boolean isFinished() {
    return isCanceled() || isSuccess() || isError();
  }

  public boolean isRunning() {
    return this == HOLD || this == WAITING || this == EXECUTING || this == CANCELLING;
  }
}
