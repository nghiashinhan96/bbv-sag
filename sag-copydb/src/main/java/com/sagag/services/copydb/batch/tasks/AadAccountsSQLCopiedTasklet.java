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
public class AadAccountsSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.AAD_ACCOUNTS";
    final String toTable = "econnectAxPCH_temp.dbo.AAD_ACCOUNTS";
    final String copiedColumns = "CREATED_DATE,FIRST_NAME,GENDER,ID,LAST_NAME,LEGAL_ENTITY_ID,PERMIT_GROUP,PERSONAL_NUMBER,PRIMARY_CONTACT_EMAIL";
    final String toColumns = "CREATED_DATE,FIRST_NAME,GENDER,ID,LAST_NAME,LEGAL_ENTITY_ID,PERMIT_GROUP,PERSONAL_NUMBER,PRIMARY_CONTACT_EMAIL";
    DbUtils.turnOnIdentityInsert(session, "AAD_ACCOUNTS");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
