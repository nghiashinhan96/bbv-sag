package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OfferAddress;
import com.sagag.services.copydb.domain.dest.DestOfferAddress;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OfferAddressCopySteps extends AbstractJobConfig {

  @Autowired
  private OfferAddressProcessor offerAddressProcessor;

  @Autowired
  private DestOfferAddressWriter offerAddressWriter;

  @Bean(name = "copyOfferAddress")
  public Step copyOfferAddress() {
    return stepBuilderFactory.get("copyOfferAddress").<OfferAddress, DestOfferAddress>chunk(DF_CHUNK)
        .reader(offerAddressReader())
        .processor(offerAddressProcessor)
        .writer(offerAddressWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OfferAddress> offerAddressReader() {
    final JpaPagingItemReader<OfferAddress> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OfferAddress e");
    reader.setName("offerAddressReader");
    return reader;
  }

}
