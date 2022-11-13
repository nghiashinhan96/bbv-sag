package com.sagag.services.tools.batch.mapping_ebl.organisation;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to define job scenario.
 */
@Configuration
@OracleProfile
public class MappingOrgIdJobConfig extends AbstractJobConfig {

  @Autowired
  private MappingOrgIdTasklet mappingOrgIdTasklet;

  @Override
  protected String jobName() {
    return "mappingOrgIdJob";
  }

  @Bean
  public Job mappingOrgIdJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get(jobName())
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(mappingOrgIdTasklet)).build();
  }

}
