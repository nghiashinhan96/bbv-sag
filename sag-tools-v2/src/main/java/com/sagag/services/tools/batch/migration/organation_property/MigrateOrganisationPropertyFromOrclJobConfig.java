package com.sagag.services.tools.batch.migration.organation_property;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;

@Configuration
@OracleProfile
public class MigrateOrganisationPropertyFromOrclJobConfig extends AbstractJobConfig {

  @Autowired
  private MigrateOrganisationPropertyFromOrclTasklet migrateOrganisationPropertyFromOrclTasklet;

  @Override
  protected String jobName() {
    return "importOrganisationPropertyListFromOrclJob";
  }

  /**
   * Job Name: importOrganisationPropertyListFromOrclJob
   */
  @Bean
  @Transactional
  public Job importOrganisationPropListFromOrclJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get(jobName())
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migrateOrganisationPropertyFromOrclTasklet)).next(restoreDbConfigStep()).build();
  }
}
