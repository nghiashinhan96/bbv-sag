package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.LicenseSettings;
import com.sagag.services.copydb.domain.dest.DestLicenseSettings;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LicenseSettingsCopySteps extends AbstractJobConfig {

  @Autowired
  private LicenseSettingsProcessor licenseSettingsProcessor;

  @Autowired
  private DestLicenseSettingsWriter licenseSettingsWriter;

  @Bean(name = "copyLicenseSettings")
  public Step copyLicenseSettings() {
    return stepBuilderFactory.get("copyLicenseSettings").<LicenseSettings, DestLicenseSettings>chunk(DF_CHUNK)
        .reader(licenseSettingsReader())
        .processor(licenseSettingsProcessor)
        .writer(licenseSettingsWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<LicenseSettings> licenseSettingsReader() {
    final JpaPagingItemReader<LicenseSettings> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from LicenseSettings e");
    reader.setName("licenseSettingsReader");
    return reader;
  }

}
