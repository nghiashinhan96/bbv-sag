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
public class SupportedAffiliateSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.SUPPORTED_AFFILIATE";
    final String toTable = "econnectAxPCH_temp.dbo.SUPPORTED_AFFILIATE";
    final String copiedColumns = "COMPANY_NAME,COUNTRY_ID,DESCRIPTION,ES_SHORT_NAME,ID,NO_REPLY_EMAIL,SALES_ORIGIN_ID,SHORT_NAME,UPDATED_DATE";
    final String toColumns = "COMPANY_NAME,COUNTRY_ID,DESCRIPTION,ES_SHORT_NAME,ID,NO_REPLY_EMAIL,SALES_ORIGIN_ID,SHORT_NAME,UPDATED_DATE";
    DbUtils.turnOnIdentityInsert(session, "SUPPORTED_AFFILIATE");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
