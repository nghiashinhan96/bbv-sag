package com.sagag.services.tools.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.PlatformTransactionManager;


public interface ISimpleStep<I, O> {

  SimpleStepBuilder<I, O> stepBuilder();

  TaskletStepBuilder taskletStepBuilder();

  PlatformTransactionManager transactionManager();

  <T extends ItemReader<I>> ItemReader<I> itemReader() throws Exception;

  ItemProcessor<I, O> itemProcessor();

  ItemWriter<O> itemWriter();

  /**
   *
   */
  default Step toStep() throws Exception {
    if (taskletStepBuilder() != null) {
      return taskletStepBuilder().build();
    }
    return simpleStepBuilder(stepBuilder()).build();
  }

  default SimpleStepBuilder<I, O> simpleStepBuilder(
      SimpleStepBuilder<I, O> stepBuilder) throws Exception {
    stepBuilder.reader(itemReader())
    .processor(itemProcessor())
    .writer(itemWriter())
    .transactionManager(transactionManager());
    return stepBuilder;
  }

}
