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
public class OfferSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.OFFER";
    final String toTable = "econnectAxPCH_temp.dbo.OFFER";
    final String copiedColumns = "ALT_OFFER_PRICE_USED,CLIENT_ID,CREATED_DATE,CREATED_USER_ID,CURRENCY_ID,DELIVERY_DATE,DELIVERY_LOCATION,ID,MODIFIED_DATE,MODIFIED_USER_ID,OFFER_DATE,OFFER_NUMBER,OWNER_ID,RECIPIENT_ADDRESS_ID,RECIPIENT_ID,REMARK,STATUS,TECSTATE,TOTAL_GROSS_PRICE,TYPE,VAT,VERSION";
    final String toColumns = "ALT_OFFER_PRICE_USED,CLIENT_ID,CREATED_DATE,CREATED_USER_ID,CURRENCY_ID,DELIVERY_DATE,DELIVERY_LOCATION,ID,MODIFIED_DATE,MODIFIED_USER_ID,OFFER_DATE,OFFER_NUMBER,OWNER_ID,RECIPIENT_ADDRESS_ID,RECIPIENT_ID,REMARK,STATUS,TECSTATE,TOTAL_GROSS_PRICE,TYPE,VAT,VERSION";
    DbUtils.turnOnIdentityInsert(session, "OFFER");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
