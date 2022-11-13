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
public class EshopPermissionSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.ESHOP_PERMISSION";
    final String toTable = "econnectAxPCH_temp.dbo.ESHOP_PERMISSION";
    final String copiedColumns = "CREATED_BY,CREATED_DATE,DESCRIPTION,ID,MODIFIED_BY,MODIFIED_DATE,PERMISSION,PERMISSION_KEY,SORT";
    final String toColumns = "CREATED_BY,CREATED_DATE,DESCRIPTION,ID,MODIFIED_BY,MODIFIED_DATE,PERMISSION,PERMISSION_KEY,SORT";
    DbUtils.turnOnIdentityInsert(session, "ESHOP_PERMISSION");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
