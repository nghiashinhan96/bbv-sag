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
public class EshopClientSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.ESHOP_CLIENT";
    final String toTable = "econnectAxPCH_temp.dbo.ESHOP_CLIENT";
    final String copiedColumns = "ACTIVE,CLIENT_NAME,CLIENT_SECRET,ID,RESOURCE_ID";
    final String toColumns = "ACTIVE,CLIENT_NAME,CLIENT_SECRET,ID,RESOURCE_ID";
    DbUtils.turnOnIdentityInsert(session, "ESHOP_CLIENT");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
