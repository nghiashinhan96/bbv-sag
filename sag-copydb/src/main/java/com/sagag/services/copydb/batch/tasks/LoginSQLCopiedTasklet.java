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
public class LoginSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.LOGIN";
    final String toTable = "econnectAxPCH_temp.dbo.LOGIN";
    final String copiedColumns = "FIRST_LOGIN_DATE,HASH_TYPE,ID,IS_USER_ACTIVE,LAST_ON_BEHALF_OF_DATE,PASSWORD,PASSWORD_HASH_BACKUP,USER_ID";
    final String toColumns = "FIRST_LOGIN_DATE,HASH_TYPE,ID,IS_USER_ACTIVE,LAST_ON_BEHALF_OF_DATE,PASSWORD,PASSWORD_HASH_BACKUP,USER_ID";
    DbUtils.turnOnIdentityInsert(session, "LOGIN");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
