package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrganisationGroup;
import com.sagag.services.copydb.domain.dest.DestOrganisationGroup;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationGroupCopySteps extends AbstractJobConfig {

  @Autowired
  private OrganisationGroupProcessor organisationGroupProcessor;

  @Autowired
  private DestOrganisationGroupWriter organisationGroupWriter;

  @Bean(name = "copyOrganisationGroup")
  public Step copyOrganisationGroup() {
    return stepBuilderFactory.get("copyOrganisationGroup").<OrganisationGroup, DestOrganisationGroup>chunk(DF_CHUNK)
        .reader(organisationGroupReader())
        .processor(organisationGroupProcessor)
        .writer(organisationGroupWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OrganisationGroup> organisationGroupReader() {
    final JpaPagingItemReader<OrganisationGroup> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OrganisationGroup e");
    reader.setName("organisationGroupReader");
    return reader;
  }

}
