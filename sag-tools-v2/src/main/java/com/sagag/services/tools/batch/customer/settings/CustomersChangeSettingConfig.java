package com.sagag.services.tools.batch.customer.settings;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.utils.ToolConstants;
import java.lang.reflect.Method;
import java.util.function.Function;
import javax.transaction.Transactional;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomersChangeSettingConfig extends AbstractJobConfig {

  @Autowired
  private CustomerSettingProperties properties;

  @Override
  protected String jobName() {
    return "changeSettingAllCustomers";
  }

  @Bean
  @Transactional
  public Job customersChangeSettingJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilder(listener).start(customersChangeSettingStep()).build();
  }

  @Bean
  public Step customersChangeSettingStep() throws Exception {
    final SimpleStepBuilder<CustomerSettings, CustomerSettings> stepBuilder =
      stepBuilderFactory.get("customersChangeSettingStep")
        .<CustomerSettings, CustomerSettings>chunk(200);
    stepBuilder.reader(customersChangeSettingItemReader());
    stepBuilder.processor(customersChangeSettingItemProcessor());
    stepBuilder.writer(customersChangeSettingItemWriter());
    stepBuilder.transactionManager(targetTransactionManager);
    return stepBuilder.build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<CustomerSettings> customersChangeSettingItemReader()
    throws BatchJobException {
    final JpaPagingItemReader<CustomerSettings> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getTargetEntityManagerFactory());
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new CustomersChangeSettingQueryProvider(properties.getAffiliate()));
    reader.setName("customersChangeSettingItemReader");
    return reader;
  }

  @Bean
  @StepScope
  public ItemProcessor<CustomerSettings, CustomerSettings> customersChangeSettingItemProcessor() {
    final String settingCol = properties.getSettingColumn();
    final Method method = CustomerSettingsTypeMapper.findMethod(settingCol);
    final Function<String, Object> typeMapper =
      CustomerSettingsTypeMapper.findTypeMapper(settingCol);
    return new CustomersChangeSettingItemProcessor(method, typeMapper,
      properties.getSettingValue());
  }

  @Bean
  @StepScope
  public JpaItemWriter<CustomerSettings> customersChangeSettingItemWriter() {
    final JpaItemWriter<CustomerSettings> writer = new CustomersChangeSettingItemWriter();
    writer.setEntityManagerFactory(getTargetEntityManagerFactory());
    return writer;
  }
}
