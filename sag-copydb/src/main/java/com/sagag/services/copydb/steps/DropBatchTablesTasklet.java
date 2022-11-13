package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractTasklet;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.utils.DbUtils;

import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import lombok.EqualsAndHashCode;

@Component
@CopyDbProfile
@EqualsAndHashCode(callSuper = false)
public class DropBatchTablesTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String[] batchTables = new String[] {
        "BATCH_JOB_EXECUTION_CONTEXT",
        "BATCH_JOB_EXECUTION_PARAMS",
        "BATCH_JOB_EXECUTION_SEQ",
        "BATCH_JOB_SEQ",
        "BATCH_STEP_EXECUTION_CONTEXT",
        "BATCH_STEP_EXECUTION_SEQ",
        "BATCH_STEP_EXECUTION",
        "BATCH_JOB_EXECUTION",
        "BATCH_JOB_INSTANCE",
    };
    DbUtils.dropTables(session, Arrays.asList(batchTables));
    return finish(contribution);
  }
}
