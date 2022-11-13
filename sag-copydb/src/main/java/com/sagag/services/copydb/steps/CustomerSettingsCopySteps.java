package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CustomerSettings;
import com.sagag.services.copydb.domain.dest.DestCustomerSettings;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CustomerSettingsCopySteps extends AbstractJobConfig {

  @Autowired
  private CustomerSettingsProcessor customerSettingsProcessor;

  @Autowired
  private DestCustomerSettingsWriter customerSettingsWriter;

  @Bean(name = "copyCustomerSettings")
  public Step copyCustomerSettings() {
    return stepBuilderFactory.get("copyCustomerSettings").<CustomerSettings, DestCustomerSettings>chunk(DF_CHUNK)
        .reader(customerSettingsReader())
        .processor(customerSettingsProcessor)
        .writer(customerSettingsWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<CustomerSettings> customerSettingsReader() {
    final JpaPagingItemReader<CustomerSettings> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from CustomerSettings e");
    reader.setName("customerSettingsReader");
    return reader;
  }

}
