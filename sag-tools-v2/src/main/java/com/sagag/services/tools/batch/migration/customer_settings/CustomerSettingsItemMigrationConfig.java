package com.sagag.services.tools.batch.migration.customer_settings;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOrganisationProperty;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.utils.ToolConstants;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import javax.transaction.Transactional;

@Configuration
@OracleProfile
public class CustomerSettingsItemMigrationConfig extends AbstractJobConfig {

  @Autowired
  private CustomerSettingsItemProcessor processor;

  @Autowired
  private CustomerSettingsItemWriter writer;

  @Value("#{'${sag.migration.customer_settings.types:}'.split(',')}")
  private List<String> types;

  @Value("#{'${sag.migration.customer.numbers:}'.split(',')}")
  private List<String> custNrs;

  @Override
  protected String jobName() {
    return "MigrateCustomerSettings";
  }

  @Bean
  @Transactional
  public Job customerSettingViewBillingFieldMigrationJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName()).incrementer(new RunIdIncrementer()).listener(listener).start(customerSettingsMigrationStep())
        .build();
  }

  //@formatter:off
  @Bean
  public Step customerSettingsMigrationStep(){
    return stepBuilderFactory.get("customerSettingsMigrationStep")
        .<SourceOrganisationProperty, CustomerSettings>chunk(DF_CHUNK)
        .reader(organisationPropertyReader())
        .processor(processor)
        .writer(writer)
        .transactionManager(targetTransactionManager).build();
  }
  //@formatter:on

  @Bean
  @StepScope
  public JpaPagingItemReader<SourceOrganisationProperty> organisationPropertyReader() {
    final JpaPagingItemReader<SourceOrganisationProperty> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getSourceEntityManagerFactory());
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new CustomerSettingsItemMigrationQueryProvider(types, custNrs));
    reader.setName("organisationPropertyReader");
    return reader;
  }
}
