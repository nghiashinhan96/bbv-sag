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
public class FeedbackSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.FEEDBACK";
    final String toTable = "econnectAxPCH_temp.dbo.FEEDBACK";
    final String copiedColumns = "CREATED_DATE,CREATED_USER_ID,FEEDBACK_MESSAGE,ID,MODIFIED_DATE,MODIFIED_USER_ID,ORG_ID,SALES_ID,SALES_INFORMATION,SOURCE,STATUS_ID,TECHNICAL_INFORMATION,TOPIC_ID,USER_ID,USER_INFORMATION";
    final String toColumns = "CREATED_DATE,CREATED_USER_ID,FEEDBACK_MESSAGE,ID,MODIFIED_DATE,MODIFIED_USER_ID,ORG_ID,SALES_ID,SALES_INFORMATION,SOURCE,STATUS_ID,TECHNICAL_INFORMATION,TOPIC_ID,USER_ID,USER_INFORMATION";
    DbUtils.turnOnIdentityInsert(session, "FEEDBACK");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
