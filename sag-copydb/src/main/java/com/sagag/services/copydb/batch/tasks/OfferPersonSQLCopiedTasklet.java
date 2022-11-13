package com.sagag.services.copydb.batch.tasks;

import com.sagag.services.copydb.batch.AbstractTasklet;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.utils.DbUtils;

import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import lombok.EqualsAndHashCode;

@Component
@CopyDbProfile
@EqualsAndHashCode(callSuper = false)
public class OfferPersonSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.OFFER_PERSON";
    final String toTable = "econnectAxPCH_temp.dbo.OFFER_PERSON";
    final String copiedColumns = "CREATED_DATE,CREATED_USER_ID,EMAIL,FIRST_NAME,HOURLY_RATE,ID,LANGUAGE_ID,LAST_NAME,MODIFIED_DATE,MODIFIED_USER_ID,OFFER_COMPANY_NAME,ORGANISATION_ID,STATUS,TECSTATE,TYPE,VERSION";
    final String toColumns = "CREATED_DATE,CREATED_USER_ID,EMAIL,FIRST_NAME,HOURLY_RATE,ID,LANGUAGE_ID,LAST_NAME,MODIFIED_DATE,MODIFIED_USER_ID,OFFER_COMPANY_NAME,ORGANISATION_ID,STATUS,TECSTATE,TYPE,VERSION";
    DbUtils.turnOnIdentityInsert(session, "OFFER_PERSON");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
