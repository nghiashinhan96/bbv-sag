package com.sagag.services.copydb.batch;

import javax.persistence.EntityManager;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import lombok.Data;

/**
 * Abstract tasklet class.
 */
@Data
public abstract class AbstractTasklet implements Tasklet {

  @Autowired
  @Qualifier("targetEntityManager")
  protected EntityManager targetEntityManager;

  protected static RepeatStatus finish(StepContribution contribution) {
    contribution.setExitStatus(ExitStatus.COMPLETED);
    return RepeatStatus.FINISHED;
  }

}
