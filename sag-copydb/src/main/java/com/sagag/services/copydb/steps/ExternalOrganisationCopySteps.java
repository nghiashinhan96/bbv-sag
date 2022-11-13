package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.ExternalOrganisation;
import com.sagag.services.copydb.domain.dest.DestExternalOrganisation;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class ExternalOrganisationCopySteps extends AbstractJobConfig {

  @Autowired
  private ExternalOrganisationProcessor externalOrganisationProcessor;

  @Autowired
  private DestExternalOrganisationWriter externalOrganisationWriter;

  @Bean(name = "copyExternalOrganisation")
  public Step copyExternalOrganisation() {
    return stepBuilderFactory.get("copyExternalOrganisation").<ExternalOrganisation, DestExternalOrganisation>chunk(DF_CHUNK)
        .reader(externalOrganisationReader())
        .processor(externalOrganisationProcessor)
        .writer(externalOrganisationWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<ExternalOrganisation> externalOrganisationReader() {
    final JpaPagingItemReader<ExternalOrganisation> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from ExternalOrganisation e");
    reader.setName("externalOrganisationReader");
    return reader;
  }

}
