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
public class MigratePersonDataFromOrclJobConfig extends AbstractJobConfig {

  @Autowired
  private MigratePersonFromOrclTasklet migratePersonFromOrclTasklet;

  @Autowired
  private MigratePersonPropertyFromOrclTasklet migratePersonPropertyFromOrclTasklet;

  @Autowired
  private MigratePersonAddressFromOrclTasklet migratePersonAddressFromOrclTasklet;

  @Override
  protected String jobName() {
    return StringUtils.EMPTY;
  }

  /**
   * Job Name:
   */
  @Bean
  @Transactional
  public Job importPersonAndPropsListFromOrclJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importEndCustomerListFromOrclJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migratePersonFromOrclTasklet))
      .next(toStep(migratePersonPropertyFromOrclTasklet))
      .next(toStep(migratePersonAddressFromOrclTasklet))
      .next(restoreDbConfigStep()).build();
  }

  /**
   * Job Name:
   */
  @Bean
  @Transactional
  public Job importPersonListFromOrclJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importPersonListFromOrclJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migratePersonFromOrclTasklet)).next(restoreDbConfigStep()).build();
  }

  /**
   * Job Name:
   */
  @Bean
  @Transactional
  public Job importPersonPropListFromOrclJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importPersonPropertyListFromOrclJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migratePersonPropertyFromOrclTasklet)).next(restoreDbConfigStep()).build();
  }

  /**
   * Job Name:
   */
  @Bean
  @Transactional
  public Job importPersonAddressListFromOrclJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importPersonAddressListFromOrclJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migratePersonAddressFromOrclTasklet)).next(restoreDbConfigStep()).build();
  }

}
