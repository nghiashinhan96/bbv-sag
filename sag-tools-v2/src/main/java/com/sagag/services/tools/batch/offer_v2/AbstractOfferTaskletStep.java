package com.sagag.services.tools.batch.offer_v2;

import com.sagag.services.tools.batch.AbstractStepConfig;
import com.sagag.services.tools.batch.ISimpleStep;

import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class AbstractOfferTaskletStep
  extends AbstractStepConfig implements ISimpleStep<Object, Object> {

  @Override
  public SimpleStepBuilder<Object, Object> stepBuilder() {
    return null;
  }

  @Override
  public PlatformTransactionManager transactionManager() {
    return targetTransactionManager;
  }

  @Override
  public ItemStreamReader<Object> itemReader() throws Exception {
    return null;
  }

  @Override
  public ItemProcessor<Object, Object> itemProcessor() {
    return null;
  }

  @Override
  public ItemWriter<Object> itemWriter() {
    return null;
  }

}
