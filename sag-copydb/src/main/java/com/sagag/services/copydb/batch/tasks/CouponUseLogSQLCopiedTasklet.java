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
public class CouponUseLogSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.COUPON_USE_LOG";
    final String toTable = "econnectAxPCH_temp.dbo.COUPON_USE_LOG";
    final String copiedColumns = "AFFILIATE_MATCH,AMOUNT_APPLIED,ARTICLE_CATEGORIES,ARTICLE_ID_MATCH,BRANDS_MATCH,COUNTRY_MATCH,COUPONS_CODE,CUSTOMER_NR,DATE_USED,DISCOUNT_ARTICLE_ID,ID,ORDER_CONFIRMATION_ID,ORDER_ID,UMAR_ID,USAGE_COUNT_REMAINDER,USER_ID";
    final String toColumns = "AFFILIATE_MATCH,AMOUNT_APPLIED,ARTICLE_CATEGORIES,ARTICLE_ID_MATCH,BRANDS_MATCH,COUNTRY_MATCH,COUPONS_CODE,CUSTOMER_NR,DATE_USED,DISCOUNT_ARTICLE_ID,ID,ORDER_CONFIRMATION_ID,ORDER_ID,UMAR_ID,USAGE_COUNT_REMAINDER,USER_ID";
    DbUtils.turnOnIdentityInsert(session, "COUPON_USE_LOG");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
