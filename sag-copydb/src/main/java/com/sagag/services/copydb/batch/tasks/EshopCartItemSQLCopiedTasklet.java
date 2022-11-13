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
public class EshopCartItemSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.ESHOP_CART_ITEM";
    final String toTable = "econnectAxPCH_temp.dbo.ESHOP_CART_ITEM";
    final String copiedColumns = "ADDED_TIME,ARTICLE_JSON,CART_KEY,CATEGORY_JSON,CHILD_JSON,CUSTOMER_NR,ID,ITEM_DESC,ITEM_TYPE,QUANTITY,SHOPTYPE,USER_ID,USER_NAME,VEHICLE_JSON";
    final String toColumns = "ADDED_TIME,ARTICLE_JSON,CART_KEY,CATEGORY_JSON,CHILD_JSON,CUSTOMER_NR,ID,ITEM_DESC,ITEM_TYPE,QUANTITY,SHOPTYPE,USER_ID,USER_NAME,VEHICLE_JSON";
    DbUtils.turnOnIdentityInsert(session, "ESHOP_CART_ITEM");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
