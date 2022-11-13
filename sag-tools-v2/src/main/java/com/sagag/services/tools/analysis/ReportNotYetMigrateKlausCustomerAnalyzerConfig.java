package com.sagag.services.tools.analysis;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;

@Configuration
public class ReportNotYetMigrateKlausCustomerAnalyzerConfig extends AbstractJobConfig {

  @Autowired
  private ReportNotYetMigrateKlausCustomerAnalyzer analyzer;

  @Override
  protected String jobName() {
    return "reportNotYetMigrateKlausCustomer";
  }

  @Bean
  @Transactional
  public Job reportNotYetMigrateKlausCustomerAnalyzerJob(
      BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilder(listener)
        .start(toStep(analyzer))
        .build();
  }

}
