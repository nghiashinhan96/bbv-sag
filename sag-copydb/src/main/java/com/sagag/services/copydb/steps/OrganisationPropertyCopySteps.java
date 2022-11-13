package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrganisationProperty;
import com.sagag.services.copydb.domain.dest.DestOrganisationProperty;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationPropertyCopySteps extends AbstractJobConfig {

  @Autowired
  private OrganisationPropertyProcessor organisationPropertyProcessor;

  @Autowired
  private DestOrganisationPropertyWriter organisationPropertyWriter;

  @Bean(name = "copyOrganisationProperty")
  public Step copyOrganisationProperty() {
    return stepBuilderFactory.get("copyOrganisationProperty").<OrganisationProperty, DestOrganisationProperty>chunk(DF_CHUNK)
        .reader(organisationPropertyReader())
        .processor(organisationPropertyProcessor)
        .writer(organisationPropertyWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OrganisationProperty> organisationPropertyReader() {
    final JpaPagingItemReader<OrganisationProperty> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OrganisationProperty e");
    reader.setName("organisationPropertyReader");
    return reader;
  }

}
