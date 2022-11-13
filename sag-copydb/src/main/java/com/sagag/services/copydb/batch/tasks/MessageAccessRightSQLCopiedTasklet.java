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
public class MessageAccessRightSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.MESSAGE_ACCESS_RIGHT";
    final String toTable = "econnectAxPCH_temp.dbo.MESSAGE_ACCESS_RIGHT";
    final String copiedColumns = "DESCRIPTION,ID,MESSAGE_ROLE_TYPE_ID,USER_GROUP,USER_GROUP_KEY";
    final String toColumns = "DESCRIPTION,ID,MESSAGE_ROLE_TYPE_ID,USER_GROUP,USER_GROUP_KEY";
    DbUtils.turnOnIdentityInsert(session, "MESSAGE_ACCESS_RIGHT");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
