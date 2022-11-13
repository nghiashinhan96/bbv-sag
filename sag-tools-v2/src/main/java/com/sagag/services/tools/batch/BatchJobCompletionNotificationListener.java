package com.sagag.services.tools.batch;

import com.sagag.services.tools.support.Folders;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.stream.Stream;

@Component
@Slf4j
public class BatchJobCompletionNotificationListener extends JobExecutionListenerSupport {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    log.info("------------------------------------------------------------------------");
    final String jobName = jobExecution.getJobInstance().getJobName();
    final Date createTime = jobExecution.getCreateTime();
    log.info("START JOB name = {} at {}", jobName, createTime);
    Stream.of(Folders.values()).forEach(folder -> {
      File dir = new File(folder.getPath());
      if (!dir.exists()) {
        dir.mkdirs();
      }
    });
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    super.afterJob(jobExecution);
    final String jobName = jobExecution.getJobInstance().getJobName();
    final Date endTime = jobExecution.getEndTime();
    final BatchStatus status = jobExecution.getStatus();
    log.info("FINISHED JOB name = {} at {} with status is {}", jobName, endTime, status);
    log.info("------------------------------------------------------------------------");
  }

}
