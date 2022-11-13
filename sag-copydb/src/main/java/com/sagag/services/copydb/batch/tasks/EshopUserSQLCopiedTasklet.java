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
public class EshopUserSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.ESHOP_USER";
    final String toTable = "econnectAxPCH_temp.dbo.ESHOP_USER";
    final String copiedColumns = "EMAIL,EMAIL_CONFIRMATION,FAX,FIRST_NAME,HOURLY_RATE,ID,LANGUAGE,LAST_NAME,NEWSLETTER,ORIGINAL_USER_ID,PHONE,SALUTATION,SETTING,SIGN_IN_DATE,TYPE,USERNAME,VAT_CONFIRM";
    final String toColumns = "EMAIL,EMAIL_CONFIRMATION,FAX,FIRST_NAME,HOURLY_RATE,ID,LANGUAGE,LAST_NAME,NEWSLETTER,ORIGINAL_USER_ID,PHONE,SALUTATION,SETTING,SIGN_IN_DATE,TYPE,USERNAME,VAT_CONFIRM";
    DbUtils.turnOnIdentityInsert(session, "ESHOP_USER");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
