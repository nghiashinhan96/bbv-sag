package com.sagag.services.tools.analysis;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;

@Configuration
public class ReportNotYetMigrateNonKlausCustomerAnalyzerConfig extends AbstractJobConfig {

  @Autowired
  private ReportNotYetMigrateNonKlausCustomerAnalyzer analyzer;

  @Override
  protected String jobName() {
    return "reportNotYetMigrateNonKlausCustomer";
  }

  @Bean
  @Transactional
  public Job reportNotYetMigrateNonKlausCustomerAnalyzerJob(
      BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilder(listener)
        .start(toStep(analyzer))
        .build();
  }

}
