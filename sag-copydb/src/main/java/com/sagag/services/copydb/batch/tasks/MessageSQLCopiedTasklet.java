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
public class MessageSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.MESSAGE";
    final String toTable = "econnectAxPCH_temp.dbo.MESSAGE";
    final String copiedColumns = "ACTIVE,CREATED_DATE,CREATED_USER_ID,DATE_VALID_FROM,DATE_VALID_TO,ID,MESSAGE_ACCESS_RIGHT_ID,MESSAGE_LOCATION_ID,MESSAGE_STYLE_ID,MESSAGE_SUB_AREA_ID,MESSAGE_TYPE_ID,MESSAGE_VISIBILITY_ID,MODIFIED_DATE,MODIFIED_USER_ID,TITLE";
    final String toColumns = "ACTIVE,CREATED_DATE,CREATED_USER_ID,DATE_VALID_FROM,DATE_VALID_TO,ID,MESSAGE_ACCESS_RIGHT_ID,MESSAGE_LOCATION_ID,MESSAGE_STYLE_ID,MESSAGE_SUB_AREA_ID,MESSAGE_TYPE_ID,MESSAGE_VISIBILITY_ID,MODIFIED_DATE,MODIFIED_USER_ID,TITLE";
    DbUtils.turnOnIdentityInsert(session, "MESSAGE");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
