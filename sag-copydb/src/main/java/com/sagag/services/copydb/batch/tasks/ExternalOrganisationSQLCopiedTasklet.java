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
public class ExternalOrganisationSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.EXTERNAL_ORGANISATION";
    final String toTable = "econnectAxPCH_temp.dbo.EXTERNAL_ORGANISATION";
    final String copiedColumns = "EXTERNAL_APP,EXTERNAL_CUSTOMER_ID,EXTERNAL_CUSTOMER_NAME,ID,ORG_ID";
    final String toColumns = "EXTERNAL_APP,EXTERNAL_CUSTOMER_ID,EXTERNAL_CUSTOMER_NAME,ID,ORG_ID";
    DbUtils.turnOnIdentityInsert(session, "EXTERNAL_ORGANISATION");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
