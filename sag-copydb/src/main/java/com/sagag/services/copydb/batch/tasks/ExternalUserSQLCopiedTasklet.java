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
public class ExternalUserSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.EXTERNAL_USER";
    final String toTable = "econnectAxPCH_temp.dbo.EXTERNAL_USER";
    final String copiedColumns = "ACTIVE,CREATED_DATE,ESHOP_USER_ID,EXTERNAL_APP,ID,LOCK_VIRTUAL_USER,PASSWORD,USERNAME";
    final String toColumns = "ACTIVE,CREATED_DATE,ESHOP_USER_ID,EXTERNAL_APP,ID,LOCK_VIRTUAL_USER,PASSWORD,USERNAME";
    DbUtils.turnOnIdentityInsert(session, "EXTERNAL_USER");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
