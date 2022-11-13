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
public class SupportedBrandPromotionSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.SUPPORTED_BRAND_PROMOTION";
    final String toTable = "econnectAxPCH_temp.dbo.SUPPORTED_BRAND_PROMOTION";
    final String copiedColumns = "ACTIVE,ARTICLE_SHOP_TYPE,BRAND,CREATED_DATE,CREATED_USER_ID,END_DATE,ID,MODIFIED_DATE,MODIFIED_USER_ID,START_DATE,SUPPORTED_AFFILIATE_ID";
    final String toColumns = "ACTIVE,ARTICLE_SHOP_TYPE,BRAND,CREATED_DATE,CREATED_USER_ID,END_DATE,ID,MODIFIED_DATE,MODIFIED_USER_ID,START_DATE,SUPPORTED_AFFILIATE_ID";
    DbUtils.turnOnIdentityInsert(session, "SUPPORTED_BRAND_PROMOTION");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
