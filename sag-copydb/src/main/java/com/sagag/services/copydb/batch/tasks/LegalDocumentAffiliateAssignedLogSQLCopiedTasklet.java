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
public class LegalDocumentAffiliateAssignedLogSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.LEGAL_DOCUMENT_AFFILIATE_ASSIGNED_LOG";
    final String toTable = "econnectAxPCH_temp.dbo.LEGAL_DOCUMENT_AFFILIATE_ASSIGNED_LOG";
    final String copiedColumns = "ACCEPTANCE_PERIOD_DAYS,AFFILIATE_ID,CREATED_DATE,CREATED_USER_ID,DATE_VALID_FROM,ID,LEGAL_DOCUMENT_ID,MODIFIED_DATE,MODIFIED_USER_ID,SORT,STATUS";
    final String toColumns = "ACCEPTANCE_PERIOD_DAYS,AFFILIATE_ID,CREATED_DATE,CREATED_USER_ID,DATE_VALID_FROM,ID,LEGAL_DOCUMENT_ID,MODIFIED_DATE,MODIFIED_USER_ID,SORT,STATUS";
    DbUtils.turnOnIdentityInsert(session, "LEGAL_DOCUMENT_AFFILIATE_ASSIGNED_LOG");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
