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
public class LegalDocumentCustomerAcceptedLogSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.LEGAL_DOCUMENT_CUSTOMER_ACCEPTED_LOG";
    final String toTable = "econnectAxPCH_temp.dbo.LEGAL_DOCUMENT_CUSTOMER_ACCEPTED_LOG";
    final String copiedColumns = "AFFILIATE_ID,CUSTOMER_ID,ID,LEGAL_DOCUMENT_ID,TIME_ACCEPTED,USER_ID";
    final String toColumns = "AFFILIATE_ID,CUSTOMER_ID,ID,LEGAL_DOCUMENT_ID,TIME_ACCEPTED,USER_ID";
    DbUtils.turnOnIdentityInsert(session, "LEGAL_DOCUMENT_CUSTOMER_ACCEPTED_LOG");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
