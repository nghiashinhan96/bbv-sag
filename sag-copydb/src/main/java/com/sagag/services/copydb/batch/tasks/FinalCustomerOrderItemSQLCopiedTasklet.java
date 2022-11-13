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
public class FinalCustomerOrderItemSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.FINAL_CUSTOMER_ORDER_ITEM";
    final String toTable = "econnectAxPCH_temp.dbo.FINAL_CUSTOMER_ORDER_ITEM";
    final String copiedColumns = "ARTICLE_DESC,ARTICLE_ID,BRAND,FINAL_CUSTOMER_ORDER_ID,GEN_ART_DESCRIPTION,GROSS_PRICE,ID,IMAGES,NET_PRICE,PRODUCT_ADDON,QUANTITY,REFERENCE,SALES_QUANTITY,SUPPLIER,TYPE,VEHICLE_DESC,VEHICLE_ID";
    final String toColumns = "ARTICLE_DESC,ARTICLE_ID,BRAND,FINAL_CUSTOMER_ORDER_ID,GEN_ART_DESCRIPTION,GROSS_PRICE,ID,IMAGES,NET_PRICE,PRODUCT_ADDON,QUANTITY,REFERENCE,SALES_QUANTITY,SUPPLIER,TYPE,VEHICLE_DESC,VEHICLE_ID";
    DbUtils.turnOnIdentityInsert(session, "FINAL_CUSTOMER_ORDER_ITEM");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
