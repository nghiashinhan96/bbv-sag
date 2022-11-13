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
public class MappingUserIdEblConnectSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.MAPPING_USER_ID_EBL_CONNECT";
    final String toTable = "econnectAxPCH_temp.dbo.MAPPING_USER_ID_EBL_CONNECT";
    final String copiedColumns = "CONNECT_ORG_ID,CONNECT_USER_ID,EBL_ORG_ID,EBL_USER_ID,ID";
    final String toColumns = "CONNECT_ORG_ID,CONNECT_USER_ID,EBL_ORG_ID,EBL_USER_ID,ID";
    DbUtils.turnOnIdentityInsert(session, "MAPPING_USER_ID_EBL_CONNECT");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
