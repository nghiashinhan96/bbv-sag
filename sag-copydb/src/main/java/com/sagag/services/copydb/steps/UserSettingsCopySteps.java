package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.UserSettings;
import com.sagag.services.copydb.domain.dest.DestUserSettings;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class UserSettingsCopySteps extends AbstractJobConfig {

  @Autowired
  private UserSettingsProcessor userSettingsProcessor;

  @Autowired
  private DestUserSettingsWriter userSettingsWriter;

  @Bean(name = "copyUserSettings")
  public Step copyUserSettings() {
    return stepBuilderFactory.get("copyUserSettings").<UserSettings, DestUserSettings>chunk(DF_CHUNK)
        .reader(userSettingsReader())
        .processor(userSettingsProcessor)
        .writer(userSettingsWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<UserSettings> userSettingsReader() {
    final JpaPagingItemReader<UserSettings> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from UserSettings e");
    reader.setName("userSettingsReader");
    return reader;
  }

}
