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
public class MessageSubAreaSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.MESSAGE_SUB_AREA";
    final String toTable = "econnectAxPCH_temp.dbo.MESSAGE_SUB_AREA";
    final String copiedColumns = "DESCRIPTION,ID,MESSAGE_AREA_ID,SORT,SUB_AREA";
    final String toColumns = "DESCRIPTION,ID,MESSAGE_AREA_ID,SORT,SUB_AREA";
    DbUtils.turnOnIdentityInsert(session, "MESSAGE_SUB_AREA");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
