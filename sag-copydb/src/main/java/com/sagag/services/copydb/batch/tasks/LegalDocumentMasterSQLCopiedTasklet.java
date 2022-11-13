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
public class LegalDocumentMasterSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.LEGAL_DOCUMENT_MASTER";
    final String toTable = "econnectAxPCH_temp.dbo.LEGAL_DOCUMENT_MASTER";
    final String copiedColumns = "COUNTRY,CREATED_DATE,CREATED_USER_ID,DOCUMENT,DOCUMENT_NAME,DOCUMENT_REF,ID,LANGUAGE,MODIFIED_DATE,MODIFIED_USER_ID,PDF_URL,SUMMARY";
    final String toColumns = "COUNTRY,CREATED_DATE,CREATED_USER_ID,DOCUMENT,DOCUMENT_NAME,DOCUMENT_REF,ID,LANGUAGE,MODIFIED_DATE,MODIFIED_USER_ID,PDF_URL,SUMMARY";
    DbUtils.turnOnIdentityInsert(session, "LEGAL_DOCUMENT_MASTER");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
