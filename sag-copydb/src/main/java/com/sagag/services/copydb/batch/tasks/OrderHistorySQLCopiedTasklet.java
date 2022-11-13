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
public class OrderHistorySQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.ORDER_HISTORY";
    final String toTable = "econnectAxPCH_temp.dbo.ORDER_HISTORY";
    final String copiedColumns = "CREATED_DATE,CUSTOMER_NUMBER,ERP_ORDER_DETAIL_URL,FINAL_CUSTOMER_ORDER_ID,GOODS_RECEIVER_ID,ID,ORDER_DATE,ORDER_INFO_JSON,ORDER_NUMBER,ORDER_STATE,SALE_ID,SALE_NAME,TOTAL,TRANS_NUMBER,USER_ID";
    final String toColumns = "CREATED_DATE,CUSTOMER_NUMBER,ERP_ORDER_DETAIL_URL,FINAL_CUSTOMER_ORDER_ID,GOODS_RECEIVER_ID,ID,ORDER_DATE,ORDER_INFO_JSON,ORDER_NUMBER,ORDER_STATE,SALE_ID,SALE_NAME,TOTAL,TRANS_NUMBER,USER_ID";
    DbUtils.turnOnIdentityInsert(session, "ORDER_HISTORY");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
