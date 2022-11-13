package com.sagag.services.tools.batch.offer_v2;

import com.sagag.services.tools.batch.AbstractStepConfig;
import com.sagag.services.tools.batch.ISimpleStep;

import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class AbstractOfferSimpleStep<I, O>
  extends AbstractStepConfig implements ISimpleStep<I, O> {

  @Override
  public TaskletStepBuilder taskletStepBuilder() {
    return null;
  }

  @Override
  public PlatformTransactionManager transactionManager() {
    return targetTransactionManager;
  }

}
