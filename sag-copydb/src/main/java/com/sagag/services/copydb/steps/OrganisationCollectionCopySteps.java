package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrganisationCollection;
import com.sagag.services.copydb.domain.dest.DestOrganisationCollection;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationCollectionCopySteps extends AbstractJobConfig {

  @Autowired
  private OrganisationCollectionProcessor organisationCollectionProcessor;

  @Autowired
  private DestOrganisationCollectionWriter organisationCollectionWriter;

  @Bean(name = "copyOrganisationCollection")
  public Step copyOrganisationCollection() {
    return stepBuilderFactory.get("copyOrganisationCollection").<OrganisationCollection, DestOrganisationCollection>chunk(DF_CHUNK)
        .reader(organisationCollectionReader())
        .processor(organisationCollectionProcessor)
        .writer(organisationCollectionWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OrganisationCollection> organisationCollectionReader() {
    final JpaPagingItemReader<OrganisationCollection> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OrganisationCollection e");
    reader.setName("organisationCollectionReader");
    return reader;
  }

}
