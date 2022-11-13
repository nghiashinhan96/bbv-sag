package com.sagag.services.tools.batch.mapping_ebl.user;

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
public class MappingUserIdJobConfig extends AbstractJobConfig {

  @Autowired
  private MappingUserIdTasklet mappingUserIdTasklet;

  @Override
  protected String jobName() {
    return "mappingUserIdJob";
  }

  @Bean
  public Job mappingUserIdJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get(jobName())
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(mappingUserIdTasklet)).build();
  }

}
