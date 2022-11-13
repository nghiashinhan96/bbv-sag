package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrgCollectionSettings;
import com.sagag.services.copydb.domain.dest.DestOrgCollectionSettings;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrgCollectionSettingsCopySteps extends AbstractJobConfig {

  @Autowired
  private OrgCollectionSettingsProcessor orgCollectionSettingsProcessor;

  @Autowired
  private DestOrgCollectionSettingsWriter orgCollectionSettingsWriter;

  @Bean(name = "copyOrgCollectionSettings")
  public Step copyOrgCollectionSettings() {
    return stepBuilderFactory.get("copyOrgCollectionSettings").<OrgCollectionSettings, DestOrgCollectionSettings>chunk(DF_CHUNK)
        .reader(orgCollectionSettingsReader())
        .processor(orgCollectionSettingsProcessor)
        .writer(orgCollectionSettingsWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OrgCollectionSettings> orgCollectionSettingsReader() {
    final JpaPagingItemReader<OrgCollectionSettings> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OrgCollectionSettings e");
    reader.setName("orgCollectionSettingsReader");
    return reader;
  }

}
