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
public class ShopArticleSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.SHOP_ARTICLE";
    final String toTable = "econnectAxPCH_temp.dbo.SHOP_ARTICLE";
    final String copiedColumns = "AMOUNT,ARTICLE_NUMBER,CREATED_DATE,CREATED_USER_ID,CURRENCY_ID,DESCRIPTION,ID,MODIFIED_DATE,MODIFIED_USER_ID,NAME,ORGANISATION_ID,PRICE,TECSTATE,TYPE,VERSION";
    final String toColumns = "AMOUNT,ARTICLE_NUMBER,CREATED_DATE,CREATED_USER_ID,CURRENCY_ID,DESCRIPTION,ID,MODIFIED_DATE,MODIFIED_USER_ID,NAME,ORGANISATION_ID,PRICE,TECSTATE,TYPE,VERSION";
    DbUtils.turnOnIdentityInsert(session, "SHOP_ARTICLE");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
