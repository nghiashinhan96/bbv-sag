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
public class MessageAccessRightRoleSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.MESSAGE_ACCESS_RIGHT_ROLE";
    final String toTable = "econnectAxPCH_temp.dbo.MESSAGE_ACCESS_RIGHT_ROLE";
    final String copiedColumns = "ESHOP_ROLE_ID,ID,MESSAGE_ACCESS_RIGHT_ID";
    final String toColumns = "ESHOP_ROLE_ID,ID,MESSAGE_ACCESS_RIGHT_ID";
    DbUtils.turnOnIdentityInsert(session, "MESSAGE_ACCESS_RIGHT_ROLE");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
