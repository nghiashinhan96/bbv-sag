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
public class FinalCustomerPropertySQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.FINAL_CUSTOMER_PROPERTY";
    final String toTable = "econnectAxPCH_temp.dbo.FINAL_CUSTOMER_PROPERTY";
    final String copiedColumns = "ID,ORG_ID,SETTING_KEY,VALUE";
    final String toColumns = "ID,ORG_ID,SETTING_KEY,VALUE";
    DbUtils.turnOnIdentityInsert(session, "FINAL_CUSTOMER_PROPERTY");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
