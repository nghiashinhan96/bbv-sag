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
public class ArticleHistorySQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.ARTICLE_HISTORY";
    final String toTable = "econnectAxPCH_temp.dbo.ARTICLE_HISTORY";
    final String copiedColumns = "ART_ID,ART_IMG,ART_NUMBER,ART_SAG_SYS_ID,DELIVERY_INFO,GROSS_PRICE,ID,MANUFACTURE,MENGE,REFERENCE,VEHICLE_INFO";
    final String toColumns = "ART_ID,ART_IMG,ART_NUMBER,ART_SAG_SYS_ID,DELIVERY_INFO,GROSS_PRICE,ID,MANUFACTURE,MENGE,REFERENCE,VEHICLE_INFO";
    DbUtils.turnOnIdentityInsert(session, "ARTICLE_HISTORY");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
