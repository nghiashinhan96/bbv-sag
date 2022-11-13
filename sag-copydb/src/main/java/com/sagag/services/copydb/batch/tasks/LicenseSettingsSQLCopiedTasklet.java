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
public class LicenseSettingsSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.LICENSE_SETTINGS";
    final String toTable = "econnectAxPCH_temp.dbo.LICENSE_SETTINGS";
    final String copiedColumns = "ID,LAST_UPDATE,LAST_UPDATED_BY,PACK_ARTICLE_ID,PACK_ARTICLE_NO,PACK_ID,PACK_NAME,PACK_UMAR_ID,PRODUCT_PARAMETERS,QUANTITY,TYPE_OF_LICENSE";
    final String toColumns = "ID,LAST_UPDATE,LAST_UPDATED_BY,PACK_ARTICLE_ID,PACK_ARTICLE_NO,PACK_ID,PACK_NAME,PACK_UMAR_ID,PRODUCT_PARAMETERS,QUANTITY,TYPE_OF_LICENSE";
    DbUtils.turnOnIdentityInsert(session, "LICENSE_SETTINGS");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
