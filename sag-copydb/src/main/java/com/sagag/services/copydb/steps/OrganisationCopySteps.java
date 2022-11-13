package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Organisation;
import com.sagag.services.copydb.domain.dest.DestOrganisation;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationCopySteps extends AbstractJobConfig {

  @Autowired
  private OrganisationProcessor organisationProcessor;

  @Autowired
  private DestOrganisationWriter organisationWriter;

  @Bean(name = "copyOrganisation")
  public Step copyOrganisation() {
    return stepBuilderFactory.get("copyOrganisation").<Organisation, DestOrganisation>chunk(DF_CHUNK)
        .reader(organisationReader())
        .processor(organisationProcessor)
        .writer(organisationWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Organisation> organisationReader() {
    final JpaPagingItemReader<Organisation> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from Organisation e");
    reader.setName("organisationReader");
    return reader;
  }

}
