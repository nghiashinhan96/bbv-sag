package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrganisationSettings;
import com.sagag.services.copydb.domain.dest.DestOrganisationSettings;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationSettingsCopySteps extends AbstractJobConfig {

  @Autowired
  private OrganisationSettingsProcessor organisationSettingsProcessor;

  @Autowired
  private DestOrganisationSettingsWriter organisationSettingsWriter;

  @Bean(name = "copyOrganisationSettings")
  public Step copyOrganisationSettings() {
    return stepBuilderFactory.get("copyOrganisationSettings").<OrganisationSettings, DestOrganisationSettings>chunk(DF_CHUNK)
        .reader(organisationSettingsReader())
        .processor(organisationSettingsProcessor)
        .writer(organisationSettingsWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OrganisationSettings> organisationSettingsReader() {
    final JpaPagingItemReader<OrganisationSettings> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OrganisationSettings e");
    reader.setName("organisationSettingsReader");
    return reader;
  }

}
