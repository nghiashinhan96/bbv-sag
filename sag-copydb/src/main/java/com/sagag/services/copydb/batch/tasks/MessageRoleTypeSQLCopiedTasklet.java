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
public class MessageRoleTypeSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.MESSAGE_ROLE_TYPE";
    final String toTable = "econnectAxPCH_temp.dbo.MESSAGE_ROLE_TYPE";
    final String copiedColumns = "DESCRIPTION,ID,ROLE_TYPE";
    final String toColumns = "DESCRIPTION,ID,ROLE_TYPE";
    DbUtils.turnOnIdentityInsert(session, "MESSAGE_ROLE_TYPE");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
