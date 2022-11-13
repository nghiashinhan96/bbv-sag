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
public class OrgCollectionSettingsSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.ORG_COLLECTION_SETTINGS";
    final String toTable = "econnectAxPCH_temp.dbo.ORG_COLLECTION_SETTINGS";
    final String copiedColumns = "COLLECTION_ID,ID,SETTING_KEY,SETTING_VALUE";
    final String toColumns = "COLLECTION_ID,ID,SETTING_KEY,SETTING_VALUE";
    DbUtils.turnOnIdentityInsert(session, "ORG_COLLECTION_SETTINGS");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
