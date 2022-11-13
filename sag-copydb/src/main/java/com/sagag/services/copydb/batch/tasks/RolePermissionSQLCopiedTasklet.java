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
public class RolePermissionSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.ROLE_PERMISSION";
    final String toTable = "econnectAxPCH_temp.dbo.ROLE_PERMISSION";
    final String copiedColumns = "ID,PERM_ID,ROLE_ID";
    final String toColumns = "ID,PERM_ID,ROLE_ID";
    DbUtils.turnOnIdentityInsert(session, "ROLE_PERMISSION");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
