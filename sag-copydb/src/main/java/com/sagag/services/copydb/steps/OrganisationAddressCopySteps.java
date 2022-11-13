package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrganisationAddress;
import com.sagag.services.copydb.domain.dest.DestOrganisationAddress;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationAddressCopySteps extends AbstractJobConfig {

  @Autowired
  private OrganisationAddressProcessor organisationAddressProcessor;

  @Autowired
  private DestOrganisationAddressWriter organisationAddressWriter;

  @Bean(name = "copyOrganisationAddress")
  public Step copyOrganisationAddress() {
    return stepBuilderFactory.get("copyOrganisationAddress").<OrganisationAddress, DestOrganisationAddress>chunk(DF_CHUNK)
        .reader(organisationAddressReader())
        .processor(organisationAddressProcessor)
        .writer(organisationAddressWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OrganisationAddress> organisationAddressReader() {
    final JpaPagingItemReader<OrganisationAddress> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OrganisationAddress e");
    reader.setName("organisationAddressReader");
    return reader;
  }

}
