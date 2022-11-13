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
public class VinLoggingSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.VIN_LOGGING";
    final String toTable = "econnectAxPCH_temp.dbo.VIN_LOGGING";
    final String copiedColumns = "CUSTOMER_ID,DATE_OF_LOG_ENTRY,ERROR_CODE,ESTIMATE_ID,ID,STATUS,USER_ID,VEHICLE_ID,VIN";
    final String toColumns = "CUSTOMER_ID,DATE_OF_LOG_ENTRY,ERROR_CODE,ESTIMATE_ID,ID,STATUS,USER_ID,VEHICLE_ID,VIN";
    DbUtils.turnOnIdentityInsert(session, "VIN_LOGGING");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
