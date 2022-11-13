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
public class BasketHistorySQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.BASKET_HISTORY";
    final String toTable = "econnectAxPCH_temp.dbo.BASKET_HISTORY";
    final String copiedColumns = "ACTIVE,BASKET_JSON,BASKET_NAME,CREATED_USER_ID,CUSTOMER_REF_TEXT,GRAND_TOTAL_EXCLUDE_VAT,ID,ORGANISATION_ID,SALES_USER_ID,UPDATED_DATE";
    final String toColumns = "ACTIVE,BASKET_JSON,BASKET_NAME,CREATED_USER_ID,CUSTOMER_REF_TEXT,GRAND_TOTAL_EXCLUDE_VAT,ID,ORGANISATION_ID,SALES_USER_ID,UPDATED_DATE";
    DbUtils.turnOnIdentityInsert(session, "BASKET_HISTORY");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
