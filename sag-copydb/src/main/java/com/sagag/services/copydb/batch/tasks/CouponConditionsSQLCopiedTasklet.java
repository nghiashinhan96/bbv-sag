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
public class CouponConditionsSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.COUPON_CONDITIONS";
    final String toTable = "econnectAxPCH_temp.dbo.COUPON_CONDITIONS";
    final String copiedColumns = "AFFILIATE,AMOUNT,ARTICLE_CATEGORIES,ARTICLE_ID,BRANDS,BULK_QUANTITY_ARTICLE_ID,BULK_QUANTITY_TRIGGER,COUNTRY,COUPON_CODE,CREATED_BY,CUSTOMER_GROUP,CUSTOMER_NR,DATE_END,DATE_OF_CREATION,DATE_OF_LAST_UPDATE,DATE_START,DISCOUNT_ARTICLE_ID,ID,MAXIMUM_DISCOUNT,MINIMUM_ORDER_AMOUNT,PERCENTAGE,SINGLE_CUSTOMER,UMAR_ID,UPDATED_BY,USAGE_COUNT,USED_COUNT";
    final String toColumns = "AFFILIATE,AMOUNT,ARTICLE_CATEGORIES,ARTICLE_ID,BRANDS,BULK_QUANTITY_ARTICLE_ID,BULK_QUANTITY_TRIGGER,COUNTRY,COUPON_CODE,CREATED_BY,CUSTOMER_GROUP,CUSTOMER_NR,DATE_END,DATE_OF_CREATION,DATE_OF_LAST_UPDATE,DATE_START,DISCOUNT_ARTICLE_ID,ID,MAXIMUM_DISCOUNT,MINIMUM_ORDER_AMOUNT,PERCENTAGE,SINGLE_CUSTOMER,UMAR_ID,UPDATED_BY,USAGE_COUNT,USED_COUNT";
    DbUtils.turnOnIdentityInsert(session, "COUPON_CONDITIONS");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
