package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrganisationType;
import com.sagag.services.copydb.domain.dest.DestOrganisationType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private OrganisationTypeProcessor organisationTypeProcessor;

  @Autowired
  private DestOrganisationTypeWriter organisationTypeWriter;

  @Bean(name = "copyOrganisationType")
  public Step copyOrganisationType() {
    return stepBuilderFactory.get("copyOrganisationType").<OrganisationType, DestOrganisationType>chunk(DF_CHUNK)
        .reader(organisationTypeReader())
        .processor(organisationTypeProcessor)
        .writer(organisationTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OrganisationType> organisationTypeReader() {
    final JpaPagingItemReader<OrganisationType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OrganisationType e");
    reader.setName("organisationTypeReader");
    return reader;
  }

}
