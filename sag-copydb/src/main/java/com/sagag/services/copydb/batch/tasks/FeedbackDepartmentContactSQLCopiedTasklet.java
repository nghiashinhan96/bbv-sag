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
public class FeedbackDepartmentContactSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.FEEDBACK_DEPARTMENT_CONTACT";
    final String toTable = "econnectAxPCH_temp.dbo.FEEDBACK_DEPARTMENT_CONTACT";
    final String copiedColumns = "DEPARTMENT_ID,ID,TYPE,VALUE";
    final String toColumns = "DEPARTMENT_ID,ID,TYPE,VALUE";
    DbUtils.turnOnIdentityInsert(session, "FEEDBACK_DEPARTMENT_CONTACT");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
