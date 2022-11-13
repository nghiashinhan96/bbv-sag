package com.sagag.services.tools.batch.offer_feature.offer;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;

/**
 * Configuration class to define job scenario.
 */
@Configuration
@OracleProfile
public class MigrateOffersJobFromCsvConfig extends AbstractJobConfig {

  @Autowired
  private MigrateOfferFromCsvTasklet migrateOfferTasklet;

  @Autowired
  private MigrateOfferPositionFromCsvTasket migrateOfferPositionFromCsvTasket;

  @Autowired
  private SplitCsvOfferFromCsvTasklet splitCsvOfferFromCsvTasklet;

  @Autowired
  private SplitCsvOfferPositionFromCsvTasklet splitCsvOfferPositionFromCsvTasklet;

  @Override
  protected String jobName() {
    return StringUtils.EMPTY;
  }

  @Bean
  @Transactional
  public Job importOfferJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importOffersJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migrateOfferTasklet)).next(restoreDbConfigStep()).build();
  }

  @Bean
  @Transactional
  public Job importOfferPositionsJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importOfferPositionsJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migrateOfferPositionFromCsvTasket)).next(restoreDbConfigStep()).build();
  }

  @Bean
  @Transactional
  public Job splitOffersJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("splitOffersJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(splitCsvOfferFromCsvTasklet)).next(restoreDbConfigStep()).build();
  }

  @Bean
  @Transactional
  public Job splitOfferPositionsJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("splitOfferPositionsJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(splitCsvOfferPositionFromCsvTasklet)).next(restoreDbConfigStep()).build();
  }

}
