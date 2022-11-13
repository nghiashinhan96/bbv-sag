package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OfferPosition;
import com.sagag.services.copydb.domain.dest.DestOfferPosition;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OfferPositionCopySteps extends AbstractJobConfig {

  @Autowired
  private OfferPositionProcessor offerPositionProcessor;

  @Autowired
  private DestOfferPositionWriter offerPositionWriter;

  @Bean(name = "copyOfferPosition")
  public Step copyOfferPosition() {
    return stepBuilderFactory.get("copyOfferPosition").<OfferPosition, DestOfferPosition>chunk(DF_CHUNK)
        .reader(offerPositionReader())
        .processor(offerPositionProcessor)
        .writer(offerPositionWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OfferPosition> offerPositionReader() {
    final JpaPagingItemReader<OfferPosition> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OfferPosition e");
    reader.setName("offerPositionReader");
    return reader;
  }

}
