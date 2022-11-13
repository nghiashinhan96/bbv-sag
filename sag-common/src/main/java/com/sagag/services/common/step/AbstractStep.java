package com.sagag.services.common.step;

import com.sagag.services.common.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public abstract class AbstractStep<I, R extends StepResult> implements Step<I, R> {

  private AbstractStep<I, R> nextStep;

  public AbstractStep<I, R> nextStep(AbstractStep<I, R> nextStep) {
    this.nextStep = nextStep;
    return this.nextStep;
  }

  public R processRequest(I input, R stepResult) throws ServiceException {
    if (shouldHandleRequest(input, stepResult)) {
      log.info(getStepName());
      stepResult = processItem(input, stepResult);
    }

    if (!stepResult.isEndProcess() && Objects.nonNull(nextStep)) {
      return nextStep.processRequest(input, stepResult);
    }

    return stepResult;
  }
}
