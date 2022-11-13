package com.sagag.services.copydb.steps;

import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.sagag.services.copydb.batch.AbstractTasklet;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.utils.DbUtils;

import lombok.EqualsAndHashCode;

@Component
@CopyDbProfile
@EqualsAndHashCode(callSuper = false)
public class OrganisationPropertyTurnOffIdentityInsertion extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    DbUtils.turnOffIdentityInsert(session, "ORGANISATION_PROPERTY");
    return finish(contribution);
  }
}
