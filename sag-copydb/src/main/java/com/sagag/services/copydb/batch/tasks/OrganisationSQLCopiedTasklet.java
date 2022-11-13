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
public class OrganisationSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.ORGANISATION";
    final String toTable = "econnectAxPCH_temp.dbo.ORGANISATION";
    final String copiedColumns = "DESCRIPTION,ID,NAME,ORDER_SETTINGS_ID,ORG_CODE,ORGTYPE_ID,PARENT_ID,SHORTNAME";
    final String toColumns = "DESCRIPTION,ID,NAME,ORDER_SETTINGS_ID,ORG_CODE,ORGTYPE_ID,PARENT_ID,SHORTNAME";
    DbUtils.turnOnIdentityInsert(session, "ORGANISATION");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
