package com.sagag.services.copydb.batch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobNotificationListener extends JobExecutionListenerSupport {

  private static final String LINE_SEPARATOR = "----------------------------------------------------------------------------";

  private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

  @Override
  public void beforeJob(JobExecution jobExecution) {
    printBannerBeforeJob(jobExecution);

  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    super.afterJob(jobExecution);
    printBannerAfterJob(jobExecution);
  }

  private void printBannerBeforeJob(JobExecution jobExecution) {
    log.info(LINE_SEPARATOR);
    final String jobName = jobExecution.getJobInstance().getJobName();
    final Date createTime = jobExecution.getCreateTime();
    log.info("---> {} JOB {} at {}", jobExecution.getStatus(), jobName, dateFormatter.format(createTime));
    log.info(LINE_SEPARATOR);
  }

  private void printBannerAfterJob(JobExecution jobExecution) {
    final String jobName = jobExecution.getJobInstance().getJobName();
    final Date createTime = jobExecution.getCreateTime();
    final Date endTime = jobExecution.getEndTime();
    long diff = endTime.getTime() - createTime.getTime();
    log.info(LINE_SEPARATOR);
    log.info("---> {} JOB {} at {}, total {}s", jobExecution.getStatus(), jobName, dateFormatter.format(endTime),
        TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS));
    log.info(LINE_SEPARATOR);
  }

}
