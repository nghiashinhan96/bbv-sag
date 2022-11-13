package com.sagag.services.tools.batch;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;

/**
 * Abstract tasklet class.
 */
@Slf4j
public abstract class AbstractTasklet implements Tasklet {

  @Value("${spring.profiles.active}")
  protected String activeProfile;

  @Value("${default.userId:}")
  protected Long defaultUserId;

  @Value("${default.organisationId:}")
  protected Integer defaultOrgId;

  @Autowired
  @Qualifier("targetEntityManager")
  protected EntityManager targetEntityManager;

  protected static RepeatStatus finish(StepContribution contribution) {
    contribution.setExitStatus(ExitStatus.COMPLETED);
    return RepeatStatus.FINISHED;
  }

  protected static RepeatStatus noOp(StepContribution contribution) {
    contribution.setExitStatus(ExitStatus.NOOP);
    return RepeatStatus.FINISHED;
  }

  protected void logActiveProfile() {
    log.debug("Active Profiles is {}", activeProfile);
    log.debug("Default user id = {}", defaultUserId);
    log.debug("Default org id = {}", defaultOrgId);
  }
}
