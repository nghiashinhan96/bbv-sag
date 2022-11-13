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
public class LicenseSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.LICENSE";
    final String toTable = "econnectAxPCH_temp.dbo.LICENSE";
    final String copiedColumns = "BEGIN_DATE,CUSTOMER_NR,END_DATE,ID,LAST_UPDATE,LAST_UPDATED_BY,LAST_USED,PACK_ID,PACK_NAME,QUANTITY,QUANTITY_USED,TYPE_OF_LICENSE,USER_ID";
    final String toColumns = "BEGIN_DATE,CUSTOMER_NR,END_DATE,ID,LAST_UPDATE,LAST_UPDATED_BY,LAST_USED,PACK_ID,PACK_NAME,QUANTITY,QUANTITY_USED,TYPE_OF_LICENSE,USER_ID";
    DbUtils.turnOnIdentityInsert(session, "LICENSE");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
