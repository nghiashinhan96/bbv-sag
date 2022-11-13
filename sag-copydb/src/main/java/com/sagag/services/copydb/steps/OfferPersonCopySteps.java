package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OfferPerson;
import com.sagag.services.copydb.domain.dest.DestOfferPerson;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OfferPersonCopySteps extends AbstractJobConfig {

  @Autowired
  private OfferPersonProcessor offerPersonProcessor;

  @Autowired
  private DestOfferPersonWriter offerPersonWriter;

  @Bean(name = "copyOfferPerson")
  public Step copyOfferPerson() {
    return stepBuilderFactory.get("copyOfferPerson").<OfferPerson, DestOfferPerson>chunk(DF_CHUNK)
        .reader(offerPersonReader())
        .processor(offerPersonProcessor)
        .writer(offerPersonWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OfferPerson> offerPersonReader() {
    final JpaPagingItemReader<OfferPerson> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OfferPerson e");
    reader.setName("offerPersonReader");
    return reader;
  }

}
