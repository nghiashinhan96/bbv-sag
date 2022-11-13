package com.sagag.services.tools.batch.offer_feature.offer_person;

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

@Configuration
@OracleProfile
public class MigratePersonDataJobConfig extends AbstractJobConfig {

  @Autowired
  private MigratePersonFromCsvTasklet migratePersonFromCsvTasklet;

  @Autowired
  private MigratePersonPropertyFromCsvTasklet migratePersonPropertyFromCsvTasklet;

  @Autowired
  private MigratePersonAddressFromCsvTasklet migratePersonAddressFromCsvTasklet;

  @Override
  protected String jobName() {
    return StringUtils.EMPTY;
  }

  /**
   * Job Name: importEndCustomerListJob
   */
  @Bean
  @Transactional
  public Job importPersonAndPropsListJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importEndCustomerListJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migratePersonFromCsvTasklet))
      .next(toStep(migratePersonPropertyFromCsvTasklet))
      .next(toStep(migratePersonAddressFromCsvTasklet))
      .next(restoreDbConfigStep()).build();
  }

  /**
   * Job Name: importPersonListJob
   */
  @Bean
  @Transactional
  public Job importPersonListJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importPersonListJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migratePersonFromCsvTasklet)).next(restoreDbConfigStep()).build();
  }

  /**
   * Job Name: importPersonPropertyListJob
   */
  @Bean
  @Transactional
  public Job importPersonPropListJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importPersonPropertyListJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migratePersonPropertyFromCsvTasklet)).next(restoreDbConfigStep()).build();
  }

  /**
   * Job Name: importPersonAddressListJob
   */
  @Bean
  @Transactional
  public Job importPersonAddressListJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importPersonAddressListJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migratePersonAddressFromCsvTasklet)).next(restoreDbConfigStep()).build();
  }

}
