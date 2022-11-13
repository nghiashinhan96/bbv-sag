package com.sagag.services.tools.batch.user.happy_points;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.batch.user.UserSettingItemWriter;
import com.sagag.services.tools.config.SagsysProfile;
import com.sagag.services.tools.domain.sagsys.SourceUserSettings;
import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.utils.ToolConstants;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@SagsysProfile
public class HappyPointUserSettingsMigrationConfig extends AbstractJobConfig {

  @Autowired
  private HappyPointUserSettingsMigrationItemProcessor itemProcessor;

  @Autowired
  private UserSettingItemWriter itemWriter;

  @Override
  protected String jobName() {
    return "CopyUserSettings";
  }

  @Bean("copyHappyPointUserSettingsJob")
  @Transactional
  public Job copyHappyPointUserSettingsJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilder(listener).start(copyHappyPointUserSettingsJobStep()).build();
  }

  @Bean
  public Step copyHappyPointUserSettingsJobStep() throws Exception {
    return stepBuilderFactory.get("copyHappyPointUserSettingsJobStep")
        .<SourceUserSettings, UserSettings>chunk(DF_CHUNK)
      .reader(userSettingsReader())
      .processor(itemProcessor)
      .writer(itemWriter)
      .transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<SourceUserSettings> userSettingsReader() {
    final JpaPagingItemReader<SourceUserSettings> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getSourceEntityManagerFactory());
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryString("select e from SourceUserSettings e");
    reader.setName("userSettingsReader");
    return reader;
  }

}
