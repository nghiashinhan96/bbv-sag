package com.sagag.services.tools.batch.cleaner.mdm_user;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.CustomExtOrganisation;
import com.sagag.services.tools.domain.target.CustomMdmCustomerAndUsersResponse;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@OracleProfile
public class CleanMdmCustomerAndUsersConfig extends AbstractJobConfig {

  @Value("#{'${batch.mdm_clean_users.customers:}'.split(',')}")
  private List<String> customerNrs;

  @Autowired
  private CleanMdmCustomerAndUsersProcessor processor;

  @Autowired
  private CleanMdmCustomerAndUsersItemWriter writer;

  @Override
  protected String jobName() {
    return "cleanMdmUsersAndCustomerJob";
  }

  @Bean
  @Transactional
  public Job cleanMdmUsersAndCustomerJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName()).incrementer(new RunIdIncrementer()).listener(listener)
      .start(cleanMdmUsersAndCustomerStep())
      .next(restoreDbConfigStep()).build();
  }

  @Bean
  public Step cleanMdmUsersAndCustomerStep() throws Exception {
    return stepBuilderFactory.get("cleanMdmUsersAndCustomerStep").<CustomExtOrganisation, CustomMdmCustomerAndUsersResponse>chunk(10)
      .reader(customerExtOrgItemReader()).processor(processor).writer(writer)
      .transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<CustomExtOrganisation> customerExtOrgItemReader() throws Exception {
    JpaPagingItemReader<CustomExtOrganisation> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(getTargetEntityManagerFactory());
    reader.setPageSize(10);
    reader.setQueryProvider(new CleanMdmCustomerAndUsersJpaQueryProvider(customerNrs));
    reader.afterPropertiesSet();
    return reader;
  }

}
