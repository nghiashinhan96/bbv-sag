package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.License;
import com.sagag.services.copydb.domain.dest.DestLicense;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LicenseCopySteps extends AbstractJobConfig {

  @Autowired
  private LicenseProcessor licenseProcessor;

  @Autowired
  private DestLicenseWriter licenseWriter;

  @Bean(name = "copyLicense")
  public Step copyLicense() {
    return stepBuilderFactory.get("copyLicense").<License, DestLicense>chunk(DF_CHUNK)
        .reader(licenseReader())
        .processor(licenseProcessor)
        .writer(licenseWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<License> licenseReader() {
    final JpaPagingItemReader<License> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from License e");
    reader.setName("licenseReader");
    return reader;
  }

}
