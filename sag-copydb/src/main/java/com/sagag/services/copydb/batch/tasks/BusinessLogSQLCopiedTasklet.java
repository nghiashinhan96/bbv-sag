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
public class BusinessLogSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.BUSINESS_LOG";
    final String toTable = "econnectAxPCH_temp.dbo.BUSINESS_LOG";
    final String copiedColumns = "API,BUSINESS_LOG_TYPE,CUSTOMER_ID,DATE_OF_LOG_ENTRY,ID,REQUEST,RESPONSE,USER_ID";
    final String toColumns = "API,BUSINESS_LOG_TYPE,CUSTOMER_ID,DATE_OF_LOG_ENTRY,ID,REQUEST,RESPONSE,USER_ID";
    DbUtils.turnOnIdentityInsert(session, "BUSINESS_LOG");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
