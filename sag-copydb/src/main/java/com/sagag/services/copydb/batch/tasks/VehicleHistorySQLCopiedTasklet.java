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
public class VehicleHistorySQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.VEHICLE_HISTORY";
    final String toTable = "econnectAxPCH_temp.dbo.VEHICLE_HISTORY";
    final String copiedColumns = "ID,ID_SAG,ID_SAG_MAKE,ID_SAG_MODEL,VEH_ID,VEH_INFO,VEH_MAKE,VEH_MODEL,VEH_NAME,VEH_TYPE,VEH_VIN";
    final String toColumns = "ID,ID_SAG,ID_SAG_MAKE,ID_SAG_MODEL,VEH_ID,VEH_INFO,VEH_MAKE,VEH_MODEL,VEH_NAME,VEH_TYPE,VEH_VIN";
    DbUtils.turnOnIdentityInsert(session, "VEHICLE_HISTORY");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
