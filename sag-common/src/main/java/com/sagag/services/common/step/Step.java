package com.sagag.services.common.step;

import com.sagag.services.common.exception.ServiceException;

public interface Step<I, R extends StepResult> {

  String getStepName();

  boolean shouldHandleRequest(I input, R stepResult) throws ServiceException;

  R processItem(I input, R stepResult) throws ServiceException;
}
