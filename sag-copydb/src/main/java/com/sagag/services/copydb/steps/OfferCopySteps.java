package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Offer;
import com.sagag.services.copydb.domain.dest.DestOffer;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OfferCopySteps extends AbstractJobConfig {

  @Autowired
  private OfferProcessor offerProcessor;

  @Autowired
  private DestOfferWriter offerWriter;

  @Bean(name = "copyOffer")
  public Step copyOffer() {
    return stepBuilderFactory.get("copyOffer").<Offer, DestOffer>chunk(DF_CHUNK)
        .reader(offerReader())
        .processor(offerProcessor)
        .writer(offerWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Offer> offerReader() {
    final JpaPagingItemReader<Offer> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from Offer e");
    reader.setName("offerReader");
    return reader;
  }

}
